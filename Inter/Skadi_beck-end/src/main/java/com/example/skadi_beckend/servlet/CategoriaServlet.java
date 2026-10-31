package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.CategoriaDAO;
import com.example.skadi_beckend.model.Categoria;
import com.example.skadi_beckend.util.Acesso;

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
        // Admin consulta o catálogo compartilhado depois de cadastrar sua CD.
        if (!Acesso.exigirAdminOuSuperAdmin(request, response)
                || !Acesso.exigirCdConfigurada(request, response)) return;
        String acao = request.getParameter("acao");
        if (acao == null) acao = "listar";

        // Criar, editar e excluir categorias são ações globais do super_admin.
        if (("novo".equals(acao) || "editar".equals(acao) || "excluir".equals(acao))
                && !Acesso.exigirSuperAdmin(request, response)) return;

        try {
            switch (acao) {
                case "novo":
                    abrirFormularioNovo(request, response);
                    break;
                case "editar":
                    abrirFormularioEdicao(request, response);
                    break;
                case "excluir":
                    excluirCategoria(request, response);
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
        // Impede alteração do catálogo por admin comum.
        if (!Acesso.exigirSuperAdmin(request, response)) return;
        String acao = request.getParameter("acao");

        if ("salvar".equals(acao)) {
            salvarCategoria(request, response);
        } else {
            response.sendRedirect("categoria?acao=listar");
        }
    }

//   Metodos auxiliares

//   Listar
    private void listarCategorias(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Categoria> lista = categoriaDao.listarTodos();
        request.setAttribute("listaCategorias", lista);
        request.setAttribute("ehSuperAdmin", Acesso.ehSuperAdmin(Acesso.usuario(request)));
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
        request.setAttribute("ehSuperAdmin", Acesso.ehSuperAdmin(Acesso.usuario(request)));
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

//   Excluir
    private void excluirCategoria(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            categoriaDao.deletar(id);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido fornecido para exclusão.");
        }
        response.sendRedirect("categoria?acao=listar");
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
            response.sendRedirect("categoria?acao=novo&erro=validacao");
            return;
        }

        try {
            double tempMax = Double.parseDouble(tempMaxParam);
            double tempMin = Double.parseDouble(tempMinParam);

            if (tempMax <= tempMin) {
                System.out.println("Validação falhou: Temperatura máxima deve ser maior que a mínima.");
                response.sendRedirect("categoria?acao=novo&erro=temperatura");
                return;
            }

            if (idParam == null || idParam.isEmpty()) {
                Categoria nova = new Categoria(nome, vidaUtilHoras, tempMax, tempMin);
                categoriaDao.inserir(nova);
            } else {
                int id = Integer.parseInt(idParam);
                Categoria atualizada = new Categoria(id, nome, vidaUtilHoras, tempMax, tempMin, null);
                categoriaDao.atualizar(atualizada);
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro na conversão de números no formulário de Categoria.");
            response.sendRedirect("categoria?acao=novo&erro=formato");
            return;
        }

        response.sendRedirect("categoria?acao=listar");
    }
}
