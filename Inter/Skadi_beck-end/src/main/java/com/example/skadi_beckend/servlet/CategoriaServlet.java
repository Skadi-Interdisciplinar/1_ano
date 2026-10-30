package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.CategoriaDAO;
import com.example.skadi_beckend.model.Categoria;

import com.example.skadi_beckend.dao.ExclusaoEmCascata;
import com.example.skadi_beckend.util.Acesso;
import com.example.skadi_beckend.util.Resposta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet responsável por gerenciar as requisições HTTP da entidade Categoria.
 */

@WebServlet("/categoria")
public class CategoriaServlet extends HttpServlet {

    private CategoriaDAO categoriaDao;

    @Override
    public void init() {
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
                case "buscarPorNome":
                    buscarPorNome(request, response);
                    break;
                case "listar":
                default:
                    listarCategorias(request, response);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Erro no GET (Categoria): " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar a solicitação.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("salvar".equals(acao)) {
            salvarCategoria(request, response);
        } else if ("excluir".equals(acao)) {
            excluirCategoria(request, response);
        } else {
            response.sendRedirect("categoria?acao=listar");
        }
    }

//   Metodos auxiliares

//   Listar
    private void listarCategorias(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Categoria> lista = categoriaDao.listarTodos();
        request.setAttribute("listaCategorias", lista);
        request.getRequestDispatcher("/lista-categoria.jsp").forward(request, response);
    }

//   Buscar por nome
    private void buscarPorNome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String trecho = request.getParameter("buscaNome");
        List<Categoria> lista;

        if (trecho != null && !trecho.trim().isEmpty()) {
            lista = categoriaDao.buscarPorNome(trecho);
        } else {
            lista = categoriaDao.listarTodos();
        }

        request.setAttribute("listaCategorias", lista);
        request.getRequestDispatcher("/lista-categoria.jsp").forward(request, response);
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/form-categoria.jsp").forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Categoria categoria = categoriaDao.buscarPorId(id);
            request.setAttribute("categoria", categoria);
            request.getRequestDispatcher("/form-categoria.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("categoria?acao=listar");
        }
    }

//   Excluir (de verdade, junto com o que estiver ligado a ele)
    private void excluirCategoria(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean apagou = false;
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            apagou = ExclusaoEmCascata.categoria(id);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido fornecido para exclusão.");
        }
        Resposta.lista(response, "categoria", apagou ? null : "exclusao");
    }

//   Salvar
    private void salvarCategoria(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String vidaUtilHoras = request.getParameter("vidaUtilHoras");
        String tempMaxParam = request.getParameter("temperaturaMax");
        String tempMinParam = request.getParameter("temperaturaMin");

        if (nome != null) nome = nome.trim();
        if (vidaUtilHoras != null) vidaUtilHoras = vidaUtilHoras.trim();

        if (nome == null || nome.isEmpty() || vidaUtilHoras == null || vidaUtilHoras.isEmpty() ||
                tempMaxParam == null || tempMinParam == null) {
            System.out.println("Validação falhou: Campos obrigatórios em branco.");
            Resposta.erroFormulario(response, "categoria", idParam, "validacao");
            return;
        }

        try {
            double tempMax = Double.parseDouble(tempMaxParam);
            double tempMin = Double.parseDouble(tempMinParam);

            if (tempMax <= tempMin) {
                System.out.println("Validação falhou: Temperatura máxima deve ser maior que a mínima.");
                Resposta.erroFormulario(response, "categoria", idParam, "temperatura");
                return;
            }

            if (idParam == null || idParam.isEmpty()) {
                Categoria nova = new Categoria(nome, vidaUtilHoras, tempMax, tempMin);
                if (!categoriaDao.inserir(nova)) {
                    Resposta.erroFormulario(response, "categoria", idParam, "banco");
                    return;
                }
            } else {
                int id = Integer.parseInt(idParam);
                Categoria atualizada = new Categoria(id, nome, vidaUtilHoras, tempMax, tempMin, null);
                if (!categoriaDao.atualizar(atualizada)) {
                    Resposta.erroFormulario(response, "categoria", idParam, "banco");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro na conversão de números no formulário de Categoria.");
            Resposta.erroFormulario(response, "categoria", idParam, "formato");
            return;
        }

        response.sendRedirect("categoria?acao=listar");
    }
}