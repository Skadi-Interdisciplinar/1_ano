package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.CategoriaDAO;
import com.example.skadi_beckend.dao.LoteDAO;
import com.example.skadi_beckend.model.Categoria;
import com.example.skadi_beckend.model.Lote;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

/**
 * Servlet responsável por gerenciar as requisições HTTP da entidade Lote.
 */

@WebServlet("/lote")
public class LoteServlet extends HttpServlet {

    private LoteDAO loteDao;
    private CategoriaDAO categoriaDao;

    @Override
    public void init() {
        this.loteDao = new LoteDAO();
        this.categoriaDao = new CategoriaDAO();
    }

//   Metodo de leitura e navegacao (listar, carregar formulario, excluir)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");
        if (acao == null) acao = "listar";

        try {
            switch (acao) {
                case "novo":
                    abrirFormularioNovo(request, response);
                    break;
                case "editar":
                    abrirFormularioEdicao(request, response);
                    break;
                case "excluir":
                    excluirLote(request, response);
                    break;
                case "buscarPorCategoria": // Requisito 8
                    buscarPorCategoria(request, response);
                    break;
                case "listar":
                default:
                    listarLotes(request, response);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Erro no GET (Lote): " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar a solicitação.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("salvar".equals(acao)) {
            salvarLote(request, response);
        } else {
            response.sendRedirect("lote?acao=listar");
        }
    }

//   Metodos auxiliares

//   Listar
    private void listarLotes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Lote> lista = loteDao.listarTodos();
        request.setAttribute("listaLotes", lista);
        request.getRequestDispatcher("/lista-lote.jsp").forward(request, response);
    }

//   Buscar por categoria
    private void buscarPorCategoria(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idCategoriaParam = request.getParameter("idCategoriaBusca");
        List<Lote> lista;

        if (idCategoriaParam != null && !idCategoriaParam.trim().isEmpty()) {
            try {
                int idCat = Integer.parseInt(idCategoriaParam);
                lista = loteDao.listarPorCategoria(idCat);
            } catch (NumberFormatException e) {
                lista = loteDao.listarTodos();
            }
        } else {
            lista = loteDao.listarTodos();
        }

        request.setAttribute("listaLotes", lista);
        request.getRequestDispatcher("/lista-lote.jsp").forward(request, response);
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Categoria> listaCategorias = categoriaDao.listarTodos();
        request.setAttribute("listaCategorias", listaCategorias);
        request.getRequestDispatcher("/form-lote.jsp").forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Lote lote = loteDao.buscarPorId(id);

            List<Categoria> listaCategorias = categoriaDao.listarTodos();
            request.setAttribute("listaCategorias", listaCategorias);
            request.setAttribute("lote", lote);

            request.getRequestDispatcher("/form-lote.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("lote?acao=listar");
        }
    }

//   Excluir
    private void excluirLote(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            loteDao.deletar(id);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido fornecido para exclusão.");
        }
        response.sendRedirect("lote?acao=listar");
    }

//   Salvar
    private void salvarLote(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        String codigoLote = request.getParameter("codigoLote");
        String idCategoriaParam = request.getParameter("idCategoria");
        String dataFabParam = request.getParameter("dataFabricacao");
        String dataValParam = request.getParameter("dataValidade");
        String status = request.getParameter("status");

        if (codigoLote != null) codigoLote = codigoLote.trim();
        if (status != null) status = status.trim().toLowerCase();

        // Validação
        if (codigoLote == null || codigoLote.isEmpty() || idCategoriaParam == null ||
                dataFabParam == null || dataFabParam.isEmpty() || dataValParam == null || dataValParam.isEmpty()) {

            System.out.println("Validação falhou: Campos obrigatórios em branco.");
            response.sendRedirect("lote?acao=novo&erro=validacao");
            return;
        }

        try {
            int idCategoria = Integer.parseInt(idCategoriaParam);
            Date dataFabricacao = Date.valueOf(dataFabParam);
            Date dataValidade = Date.valueOf(dataValParam);

            // Validação
            if (dataValidade.before(dataFabricacao)) {
                System.out.println("Validação falhou: Data de validade anterior à fabricação.");
                response.sendRedirect("lote?acao=novo&erro=datas");
                return;
            }

            if (idParam == null || idParam.isEmpty()) {
                Lote novo = new Lote(codigoLote, idCategoria, dataFabricacao, dataValidade, status);
                loteDao.inserir(novo);
            } else {
                int id = Integer.parseInt(idParam);
                Lote atualizado = new Lote(id, codigoLote, idCategoria, dataFabricacao, dataValidade, status, null);
                loteDao.atualizar(atualizado);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Erro na conversão de formato de número ou data.");
            response.sendRedirect("lote?acao=novo&erro=formato");
            return;
        }

        response.sendRedirect("lote?acao=listar");
    }
}