package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.CDDAO;
import com.example.skadi_beckend.dao.FrigorificoDAO;
import com.example.skadi_beckend.model.CD;
import com.example.skadi_beckend.model.Frigorifico;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 - Servlet responsável por gerenciar as requisições HTTP da entidade Frigorifico.

 */
@WebServlet("/frigorifico")
public class FrigorificoServlet extends HttpServlet {

    private FrigorificoDAO frigorificoDao;
    private CDDAO cdDao;

    @Override
    public void init() {
        this.frigorificoDao = new FrigorificoDAO();
        this.cdDao = new CDDAO();
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
                    excluirFrigorifico(request, response);
                    break;
                case "excluirPorLocalizacao":
                    excluirPorLocalizacao(request, response);
                    break;
                case "buscarPorNome":
                    buscarPorNome(request, response);
                    break;
                case "listarAtivos":
                    listarComTermometrosAtivos(request, response);
                    break;
                case "listar":
                default:
                    listarFrigorificos(request, response);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Erro no GET (Frigorifico): " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar o pedido.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("salvar".equals(acao)) {
            salvarFrigorifico(request, response);
        } else {
            response.sendRedirect("frigorifico?acao=listar");
        }
    }

//    Metodos auxiliares

    //   Listar
    private void listarFrigorificos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Frigorifico> lista = frigorificoDao.listarTodos();
        request.setAttribute("listaFrigorificos", lista);
        request.getRequestDispatcher("/lista-frigorifico.jsp").forward(request, response);
    }

    //   Buscar por nome
    private void buscarPorNome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String trechoNome = request.getParameter("buscaNome");
        List<Frigorifico> lista;

        if (trechoNome != null && !trechoNome.trim().isEmpty()) {
            lista = frigorificoDao.buscarPorNome(trechoNome);
        } else {
            lista = frigorificoDao.listarTodos();
        }

        request.setAttribute("listaFrigorificos", lista);
        request.getRequestDispatcher("/lista-frigorifico.jsp").forward(request, response);
    }

    //   Listar com termometros ativos
    private void listarComTermometrosAtivos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Frigorifico> lista = frigorificoDao.listarComTermometrosAtivos();
        request.setAttribute("listaFrigorificos", lista);
        request.getRequestDispatcher("/lista-frigorifico.jsp").forward(request, response);
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<CD> listaCds = cdDao.listarTodos();
        request.setAttribute("listaCds", listaCds);
        request.getRequestDispatcher("/form-frigorifico.jsp").forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Frigorifico frigorifico = frigorificoDao.buscarPorId(id);

            List<CD> listaCds = cdDao.listarTodos();
            request.setAttribute("listaCds", listaCds);
            request.setAttribute("frigorifico", frigorifico);

            request.getRequestDispatcher("/form-frigorifico.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("frigorifico?acao=listar");
        }
    }

    //   Excluir
    private void excluirFrigorifico(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            frigorificoDao.deletar(id);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido fornecido para exclusão.");
        }
        response.sendRedirect("frigorifico?acao=listar");
    }

    //   Excluir por localizacao
    private void excluirPorLocalizacao(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String localizacao = request.getParameter("localizacao");
        if (localizacao != null && !localizacao.trim().isEmpty()) {
            frigorificoDao.deletarPorLocalizacao(localizacao);
        }
        response.sendRedirect("frigorifico?acao=listar");
    }

    //   Salvar
    private void salvarFrigorifico(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String localizacao = request.getParameter("localizacao");
        String idCdParam = request.getParameter("idCd");
        String tempMaxParam = request.getParameter("temperaturaMax");
        String tempMinParam = request.getParameter("temperaturaMin");


        if (nome == null || nome.trim().isEmpty() || localizacao == null || localizacao.trim().isEmpty() || idCdParam == null || tempMaxParam == null || tempMinParam == null) {
            response.sendRedirect("frigorifico?acao=novo&erro=validacao");
            return;
        }

        try {
            int idCd = Integer.parseInt(idCdParam);
            Double tempMax = Double.parseDouble(tempMaxParam);
            Double tempMin = Double.parseDouble(tempMinParam);

            if (idParam == null || idParam.isEmpty()) {
                Frigorifico novo = new Frigorifico(nome, localizacao, idCd, tempMax, tempMin);
                frigorificoDao.inserir(novo);
            } else {
                int id = Integer.parseInt(idParam);
                Frigorifico atualizado = new Frigorifico(id, nome, localizacao, idCd, tempMax, tempMin);
                frigorificoDao.atualizar(atualizado);
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro na conversão de números no formulário.");
        }

        response.sendRedirect("frigorifico?acao=listar");
    }
}