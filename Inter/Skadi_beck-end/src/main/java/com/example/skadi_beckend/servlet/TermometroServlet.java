package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.FrigorificoDAO;
import com.example.skadi_beckend.dao.TermometroDAO;
import com.example.skadi_beckend.model.Frigorifico;
import com.example.skadi_beckend.model.Termometro;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet responsável por gerenciar as requisições HTTP da entidade Termometro.
 */

@WebServlet("/termometro")
public class TermometroServlet extends HttpServlet {

    private TermometroDAO termometroDao;
    private FrigorificoDAO frigorificoDao;

    @Override
    public void init() {
        this.termometroDao = new TermometroDAO();
        this.frigorificoDao = new FrigorificoDAO();
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
                    excluirTermometro(request, response);
                    break;
                case "buscarPorFrigorifico": // Requisito 8 - Filtro customizado
                    buscarPorFrigorifico(request, response);
                    break;
                case "listar":
                default:
                    listarTermometros(request, response);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Erro no GET (Termometro): " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar a solicitação.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("salvar".equals(acao)) {
            salvarTermometro(request, response);
        } else {
            response.sendRedirect("termometro?acao=listar");
        }
    }

//   Metodos auxiliares

//   Listar
    private void listarTermometros(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Termometro> lista = termometroDao.listarTodos();
        request.setAttribute("listaTermometros", lista);
        request.getRequestDispatcher("/lista-termometro.jsp").forward(request, response);
    }

//   Buscar por termometros ligados a frigorificos
    private void buscarPorFrigorifico(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idFrigorificoParam = request.getParameter("idFrigorificoBusca");
        List<Termometro> lista;

        if (idFrigorificoParam != null && !idFrigorificoParam.trim().isEmpty()) {
            try {
                int idFrig = Integer.parseInt(idFrigorificoParam);
                lista = termometroDao.listarPorFrigorifico(idFrig);
            } catch (NumberFormatException e) {
                lista = termometroDao.listarTodos();
            }
        } else {
            lista = termometroDao.listarTodos();
        }

        request.setAttribute("listaTermometros", lista);
        request.getRequestDispatcher("/lista-termometro.jsp").forward(request, response);
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Frigorifico> listaFrigorificos = frigorificoDao.listarTodos();
        request.setAttribute("listaFrigorificos", listaFrigorificos);
        request.getRequestDispatcher("/form-termometro.jsp").forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Termometro termometro = termometroDao.buscarPorId(id);

            List<Frigorifico> listaFrigorificos = frigorificoDao.listarTodos();
            request.setAttribute("listaFrigorificos", listaFrigorificos);
            request.setAttribute("termometro", termometro);

            request.getRequestDispatcher("/form-termometro.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("termometro?acao=listar");
        }
    }

//   Excluir
    private void excluirTermometro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            termometroDao.deletar(id);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido fornecido para exclusão.");
        }
        response.sendRedirect("termometro?acao=listar");
    }

//   Salvar
    private void salvarTermometro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        String modelo = request.getParameter("modelo");
        String status = request.getParameter("status");
        String idFrigorificoParam = request.getParameter("idFrigorifico");

        if (modelo != null) modelo = modelo.trim();
        if (status != null) status = status.trim().toLowerCase();

        // Validação
        if (modelo == null || modelo.isEmpty() || status == null || status.isEmpty() || idFrigorificoParam == null) {
            System.out.println("Validação falhou: Campos obrigatórios em branco.");
            response.sendRedirect("termometro?acao=novo&erro=validacao");
            return;
        }

        try {
            int idFrigorifico = Integer.parseInt(idFrigorificoParam);

            if (idParam == null || idParam.isEmpty()) {
                Termometro novo = new Termometro(modelo, status, idFrigorifico);
                termometroDao.inserir(novo);
            } else {
                int id = Integer.parseInt(idParam);
                Termometro atualizado = new Termometro(id, modelo, status, idFrigorifico, null);
                termometroDao.atualizar(atualizado);
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro na conversão de números no formulário.");
        }

        response.sendRedirect("termometro?acao=listar");
    }
}