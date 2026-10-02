package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.ProdutoDAO;
import com.example.skadi_beckend.dao.FrigorificoDAO;
import com.example.skadi_beckend.model.Produto;
import com.example.skadi_beckend.model.Frigorifico;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/produto")
public class ProdutoServlet extends HttpServlet {

    private ProdutoDAO produtoDAO;
    private FrigorificoDAO refrigeradorDAO; // Para a chave estrangeira

    @Override
    public void init() throws ServletException {
        this.produtoDAO = new ProdutoDAO();
        this.refrigeradorDAO = new FrigorificoDAO();
    }

//    Exibir telas
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "listar";
        }

        try {
            if (action.equals("deletar")) {
                int id = Integer.parseInt(request.getParameter("id"));
                produtoDAO.deletar(id);
                response.sendRedirect("produto?action=listar");

            } else if (action.equals("novo") || action.equals("editar")) {
                // Busca a lista de Refrigeradores
                List<Frigorifico> listaRefrigeradores = refrigeradorDAO.listarTodos();
                request.setAttribute("listaRefrigeradores", listaRefrigeradores);

                if (action.equals("editar")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Produto produto = produtoDAO.buscarPorId(id);
                    request.setAttribute("produtoParaEditar", produto);
                }

                // Vai para a tela de cadastro
                request.getRequestDispatcher("cadastroProduto.jsp").forward(request, response);

            } else {
                // Filtar por categoria
                String categoriaParam = request.getParameter("categoria");
                List<Produto> lista;

                if (categoriaParam != null && !categoriaParam.isEmpty()) {
                    lista = produtoDAO.buscarPorCategoria(categoriaParam);
                } else {
                    lista = produtoDAO.listarTodos();
                }

                request.setAttribute("listaProdutos", lista);

                request.getRequestDispatcher("listaProdutos.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("Erro no banco de dados: " + e.getMessage());
        }
    }

//    Receber em html
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Pegando os dados digitados
        String idStr = request.getParameter("id_produto");
        String nome = request.getParameter("nome");
        String categoria = request.getParameter("categoria");

        // Conversão
        double tempIdeal = Double.parseDouble(request.getParameter("temperatura_ideal"));

        Date validade = Date.valueOf(request.getParameter("validade"));

        int idRefrigerador = Integer.parseInt(request.getParameter("id_refrigerador"));

        // Checar o tempo de sobrevivencia
        String tempoStr = request.getParameter("tempo_sobrevivencia");
        Integer tempoSobrevivencia = (tempoStr != null && !tempoStr.isEmpty()) ? Integer.parseInt(tempoStr) : null;

        // Montando o Produto
        Produto produto = new Produto();
        produto.setNome(nome);
        produto.setCategoria(categoria);
        produto.setTemperatura_ideal(tempIdeal);
        produto.setValidade(validade);
        produto.setTempo_sobrevivencia(tempoSobrevivencia);
        produto.setId_refrigerador(idRefrigerador);

        try {
            if (idStr == null || idStr.isEmpty()) {
                produtoDAO.inserir(produto);
            } else {
                produto.setId_produto(Integer.parseInt(idStr));
                produtoDAO.atualizar(produto);
            }

            response.sendRedirect("produto?action=listar");

        } catch (Exception e) {
            throw new ServletException("Erro ao salvar Produto: " + e.getMessage());
        }
    }
}