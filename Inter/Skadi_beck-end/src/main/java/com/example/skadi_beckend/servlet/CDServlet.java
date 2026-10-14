package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.CDDAO;
import com.example.skadi_beckend.model.CD;
import com.example.skadi_beckend.util.Validator; // <-- IMPORTAÇÃO DA SUA CLASSE

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet responsável por gerenciar as requisições HTTP da entidade CD.
 */
@WebServlet("/cd")
public class CDServlet extends HttpServlet {

    private CDDAO dao;

    @Override
    public void init() {
        this.dao = new CDDAO();
    }

//    Metodo de leitura e navegacao (listar, carregar formulario, excluir)
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
                    excluirCD(request, response);
                    break;
                case "excluirPorCnpj":
                    excluirCDPorCnpj(request, response);
                    break;
                case "buscarPorNome":
                    buscarPorNome(request, response);
                    break;
                case "listar":
                default:
                    listarCDs(request, response);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Erro no processamento da requisição GET (CD): " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ocorreu um erro ao processar sua solicitação.");
        }
    }

//    Metodo enviar dados do formulario (salvar / atualizar)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("salvar".equals(acao)) {
            salvarCD(request, response);
        } else {
            response.sendRedirect("cd?acao=listar");
        }
    }

//    Metodos auxiliares

//   Listar
    private void listarCDs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<CD> listaCds = dao.listarTodos();
        request.setAttribute("listaCds", listaCds);
        request.getRequestDispatcher("/lista-cd.jsp").forward(request, response);
    }

//   Buscar por nome
    private void buscarPorNome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String trechoNome = request.getParameter("buscaNome");
        List<CD> listaCds;

        if (trechoNome != null && !trechoNome.trim().isEmpty()) {
            listaCds = dao.buscarPorNome(trechoNome);
        } else {
            listaCds = dao.listarTodos();
        }

        request.setAttribute("listaCds", listaCds);
        request.getRequestDispatcher("/lista-cd.jsp").forward(request, response);
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/form-cd.jsp").forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            CD cdExistente = dao.buscarPorId(id);

            request.setAttribute("cd", cdExistente);
            request.getRequestDispatcher("/form-cd.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            System.out.println("Erro de formatação ao buscar ID para edição: " + e.getMessage());
            response.sendRedirect("cd?acao=listar");
        }
    }

//   Excluir CD
    private void excluirCD(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.deletar(id);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido fornecido para exclusão.");
        }
        response.sendRedirect("cd?acao=listar");
    }

//   Excluir por CNPJ
    private void excluirCDPorCnpj(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String cnpj = request.getParameter("cnpj");


        if (Validator.validarFormatoCnpj(cnpj)) {
            // Limpa a pontuação antes de mandar para o banco
            cnpj = cnpj.replaceAll("\\D", "");
            dao.deletarPorCnpj(cnpj);
        }
        response.sendRedirect("cd?acao=listar");
    }

//   Salvar
    private void salvarCD(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String cnpj = request.getParameter("cnpj");


        if (nome == null || nome.trim().isEmpty() || !Validator.validarFormatoCnpj(cnpj)) {
            System.out.println("Validação falhou: Nome vazio ou CNPJ inválido.");
            response.sendRedirect("cd?acao=novo&erro=validacao");
            return;
        }

        // LIMPA A PONTUAÇÃO DO CNPJ ANTES DE SALVAR NO BANCO
        cnpj = cnpj.replaceAll("\\D", "");

        if (idParam == null || idParam.isEmpty()) {
            CD novoCD = new CD(nome, cnpj);
            dao.inserir(novoCD);
        } else {
            try {
                int id = Integer.parseInt(idParam);
                CD cdAtualizado = new CD(id, nome, cnpj);
                dao.atualizar(cdAtualizado);
            } catch (NumberFormatException e) {
                System.out.println("Erro ao converter ID no momento de salvar.");
            }
        }
        response.sendRedirect("cd?acao=listar");
    }
}