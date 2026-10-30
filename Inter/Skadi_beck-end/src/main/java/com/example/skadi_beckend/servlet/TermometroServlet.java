package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.FrigorificoDAO;
import com.example.skadi_beckend.dao.TermometroDAO;
import com.example.skadi_beckend.model.Frigorifico;
import com.example.skadi_beckend.model.Termometro;

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
        } else if ("excluir".equals(acao)) {
            excluirTermometro(request, response);
        } else {
            response.sendRedirect("termometro?acao=listar");
        }
    }

//   Metodos auxiliares

//   O termômetro não guarda o CD: ele pertence à unidade do frigorífico onde está
    private List<Termometro> daUnidade(HttpServletRequest request, List<Termometro> todos) {
        java.util.Set<Integer> frigorificos = new java.util.HashSet<>();
        for (Frigorifico f : Acesso.daUnidade(request, frigorificoDao.listarTodos(), Frigorifico::getIdCd)) {
            frigorificos.add(f.getIdFrigorifico());
        }
        List<Termometro> filtrados = new java.util.ArrayList<>();
        for (Termometro t : todos) {
            if (frigorificos.contains(t.getIdFrigorifico())) {
                filtrados.add(t);
            }
        }
        return filtrados;
    }

//   Listar
    private void listarTermometros(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Termometro> lista = daUnidade(request, termometroDao.listarTodos());
        request.setAttribute("listaTermometros", lista);
        // A tela mostra o nome do frigorífico de cada termômetro
        request.setAttribute("listaFrigorificos", Acesso.daUnidade(request, frigorificoDao.listarTodos(), Frigorifico::getIdCd));
        request.getRequestDispatcher("/lista-termometro.jsp").forward(request, response);
    }

//   Buscar por termometros ligados a frigorificos
    private void buscarPorFrigorifico(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idFrigorificoParam = request.getParameter("idFrigorificoBusca");
        List<Termometro> lista;

        if (idFrigorificoParam != null && !idFrigorificoParam.trim().isEmpty()) {
            try {
                int idFrig = Integer.parseInt(idFrigorificoParam);
                lista = daUnidade(request, termometroDao.listarPorFrigorifico(idFrig));
            } catch (NumberFormatException e) {
                lista = daUnidade(request, termometroDao.listarTodos());
            }
        } else {
            lista = daUnidade(request, termometroDao.listarTodos());
        }

        request.setAttribute("listaTermometros", lista);
        request.setAttribute("listaFrigorificos", Acesso.daUnidade(request, frigorificoDao.listarTodos(), Frigorifico::getIdCd));
        request.getRequestDispatcher("/lista-termometro.jsp").forward(request, response);
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Frigorifico> listaFrigorificos = Acesso.daUnidade(request, frigorificoDao.listarTodos(), Frigorifico::getIdCd);
        request.setAttribute("listaFrigorificos", listaFrigorificos);
        request.getRequestDispatcher("/form-termometro.jsp").forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Termometro termometro = termometroDao.buscarPorId(id);

            List<Frigorifico> listaFrigorificos = Acesso.daUnidade(request, frigorificoDao.listarTodos(), Frigorifico::getIdCd);
            request.setAttribute("listaFrigorificos", listaFrigorificos);
            request.setAttribute("termometro", termometro);

            request.getRequestDispatcher("/form-termometro.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("termometro?acao=listar");
        }
    }

//   Excluir (de verdade, junto com o que estiver ligado a ele)
    private void excluirTermometro(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean apagou = false;
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            apagou = ExclusaoEmCascata.termometro(id);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido fornecido para exclusão.");
        }
        Resposta.lista(response, "termometro", apagou ? null : "exclusao");
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
            Resposta.erroFormulario(response, "termometro", idParam, "validacao");
            return;
        }

        try {
            int idFrigorifico = Integer.parseInt(idFrigorificoParam);

            if (idParam == null || idParam.isEmpty()) {
                Termometro novo = new Termometro(modelo, status, idFrigorifico);
                if (!termometroDao.inserir(novo)) {
                    Resposta.erroFormulario(response, "termometro", idParam, "banco");
                    return;
                }
            } else {
                int id = Integer.parseInt(idParam);
                Termometro atualizado = new Termometro(id, modelo, status, idFrigorifico, null);
                if (!termometroDao.atualizar(atualizado)) {
                    Resposta.erroFormulario(response, "termometro", idParam, "banco");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro na conversão de números no formulário.");
        }

        response.sendRedirect("termometro?acao=listar");
    }
}