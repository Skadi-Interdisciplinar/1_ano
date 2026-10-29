package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.CDDAO;
import com.example.skadi_beckend.model.CD;
import com.example.skadi_beckend.model.Usuario;
import com.example.skadi_beckend.util.Validator; // <-- IMPORTAÇÃO DA SUA CLASSE

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
                case "selecionar":
                    selecionarCD(request, response);
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
        } else if ("excluir".equals(acao)) {
            excluirCD(request, response);
        } else {
            response.sendRedirect("cd?acao=listar");
        }
    }

//    Metodos auxiliares

//   Listar
    private void listarCDs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<CD> listaCds = Acesso.cdsPermitidos(request);
        request.setAttribute("listaCds", listaCds);
        request.getRequestDispatcher("/lista-cd.jsp").forward(request, response);
    }

//   Buscar por nome
    private void buscarPorNome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String trechoNome = request.getParameter("buscaNome");
        List<CD> listaCds;

        if (trechoNome != null && !trechoNome.trim().isEmpty()) {
            listaCds = new java.util.ArrayList<>();
            for (CD cd : dao.buscarPorNome(trechoNome)) {
                if (Acesso.cdPermitido(request, cd.getIdCd())) {
                    listaCds.add(cd);
                }
            }
        } else {
            listaCds = Acesso.cdsPermitidos(request);
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

//   Escolher a unidade: guarda na sessão e abre a lista de funcionários dela
    private void selecionarCD(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            CD cd = Acesso.cdPermitido(request, id) ? dao.buscarPorId(id) : null;
            if (cd != null) {
                Acesso.definirCdAtual(request, cd);
                response.sendRedirect("usuario?acao=listar");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido ao escolher a unidade.");
        }
        response.sendRedirect("cd?acao=listar");
    }

//   Excluir (de verdade, junto com o que estiver ligado a ele)
    private void excluirCD(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean apagou = false;
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            apagou = ExclusaoEmCascata.cd(id);
            if (apagou) {
                Integer atual = Acesso.cdAtual(request);
                if (atual != null && atual == id) {
                    Acesso.limparCdAtual(request);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido fornecido para exclusão.");
        }
        Resposta.lista(response, "cd", apagou ? null : "exclusao");
    }

//   Salvar
    private void salvarCD(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String cnpj = request.getParameter("cnpj");


        if (nome == null || nome.trim().isEmpty() || !Validator.validarFormatoCnpj(cnpj)) {
            System.out.println("Validação falhou: Nome vazio ou CNPJ inválido.");
            Resposta.erroFormulario(response, "cd", idParam, "validacao");
            return;
        }

        // LIMPA A PONTUAÇÃO DO CNPJ ANTES DE SALVAR NO BANCO
        cnpj = cnpj.replaceAll("\\D", "");

        if (idParam == null || idParam.isEmpty()) {
            // Se quem cadastra é admin, a unidade nova já entra na lista dele
            Usuario logado = Acesso.logado(request);
            Integer idAdmin = Acesso.ehAdmin(logado) ? logado.getIdUsuario() : null;
            if (dao.inserirERetornarId(new CD(nome, cnpj), idAdmin) == 0) {
                Resposta.erroFormulario(response, "cd", idParam, "banco");
                return;
            }
        } else {
            try {
                int id = Integer.parseInt(idParam);
                CD cdAtualizado = new CD(id, nome, cnpj);
                if (!dao.atualizar(cdAtualizado)) {
                    Resposta.erroFormulario(response, "cd", idParam, "banco");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro ao converter ID no momento de salvar.");
            }
        }
        response.sendRedirect("cd?acao=listar");
    }
}