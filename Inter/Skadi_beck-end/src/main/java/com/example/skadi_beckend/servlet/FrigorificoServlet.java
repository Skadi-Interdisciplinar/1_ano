package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.CDDAO;
import com.example.skadi_beckend.dao.FrigorificoDAO;
import com.example.skadi_beckend.model.CD;
import com.example.skadi_beckend.model.Frigorifico;
import com.example.skadi_beckend.model.Usuario;
import com.example.skadi_beckend.util.Acesso;

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
        // Só administradores com CD configurada acessam frigoríficos.
        if (!Acesso.exigirAdminOuSuperAdmin(request, response)
                || !Acesso.exigirCdConfigurada(request, response)) return;
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
        // Confere a permissão novamente no envio do formulário.
        if (!Acesso.exigirAdminOuSuperAdmin(request, response)
                || !Acesso.exigirCdConfigurada(request, response)) return;
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
        Usuario ator = Acesso.usuario(request);
        // Admin vê os frigoríficos da própria CD; super_admin vê todos.
        List<Frigorifico> lista = Acesso.ehSuperAdmin(ator)
                ? frigorificoDao.listarTodos() : frigorificoDao.listarPorCd(ator.getIdCd());
        request.setAttribute("listaFrigorificos", lista);
        request.getRequestDispatcher("/lista-frigorifico.jsp").forward(request, response);
    }

    //   Buscar por nome
    private void buscarPorNome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String trechoNome = request.getParameter("buscaNome");
        Usuario ator = Acesso.usuario(request);
        List<Frigorifico> lista;

        // Busca por nome continua limitada à unidade do admin.
        if (Acesso.ehSuperAdmin(ator) && trechoNome != null && !trechoNome.trim().isEmpty()) {
            lista = frigorificoDao.buscarPorNome(trechoNome);
        } else if (Acesso.ehSuperAdmin(ator)) {
            lista = frigorificoDao.listarTodos();
        } else if (trechoNome != null && !trechoNome.trim().isEmpty()) {
            lista = frigorificoDao.buscarPorNomeECd(trechoNome, ator.getIdCd());
        } else {
            lista = frigorificoDao.listarPorCd(ator.getIdCd());
        }

        request.setAttribute("listaFrigorificos", lista);
        request.getRequestDispatcher("/lista-frigorifico.jsp").forward(request, response);
    }

    //   Listar com termometros ativos
    private void listarComTermometrosAtivos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario ator = Acesso.usuario(request);
        List<Frigorifico> lista = Acesso.ehSuperAdmin(ator)
                ? frigorificoDao.listarComTermometrosAtivos()
                : frigorificoDao.listarComTermometrosAtivosPorCd(ator.getIdCd());
        request.setAttribute("listaFrigorificos", lista);
        request.getRequestDispatcher("/lista-frigorifico.jsp").forward(request, response);
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario ator = Acesso.usuario(request);
        // Admin recebe sua CD; super_admin pode escolher qualquer unidade.
        List<CD> listaCds = Acesso.ehSuperAdmin(ator)
                ? cdDao.listarTodos()
                : java.util.Collections.singletonList(cdDao.buscarPorId(ator.getIdCd()));
        request.setAttribute("listaCds", listaCds);
        request.getRequestDispatcher("/form-frigorifico.jsp").forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Usuario ator = Acesso.usuario(request);
            // Confere o vínculo com a CD antes de carregar o formulário de edição.
            Frigorifico frigorifico = Acesso.ehSuperAdmin(ator)
                    ? frigorificoDao.buscarPorId(id)
                    : frigorificoDao.buscarPorIdECd(id, ator.getIdCd());
            if (frigorifico == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Frigorífico fora da sua CD.");
                return;
            }

            List<CD> listaCds = Acesso.ehSuperAdmin(ator)
                    ? cdDao.listarTodos()
                    : java.util.Collections.singletonList(cdDao.buscarPorId(ator.getIdCd()));
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
            Usuario ator = Acesso.usuario(request);
            // Admin exclui apenas dentro da própria CD.
            if (Acesso.ehSuperAdmin(ator)) frigorificoDao.deletar(id);
            else frigorificoDao.deletarPorIdECd(id, ator.getIdCd());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido fornecido para exclusão.");
        }
        response.sendRedirect("frigorifico?acao=listar");
    }

    //   Excluir por localizacao
    private void excluirPorLocalizacao(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String localizacao = request.getParameter("localizacao");
        if (localizacao != null && !localizacao.trim().isEmpty()) {
            Usuario ator = Acesso.usuario(request);
            if (Acesso.ehSuperAdmin(ator)) frigorificoDao.deletarPorLocalizacao(localizacao);
            else frigorificoDao.deletarPorLocalizacaoECd(localizacao, ator.getIdCd());
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


        Usuario ator = Acesso.usuario(request);
        if (nome == null || nome.trim().isEmpty() || localizacao == null || localizacao.trim().isEmpty()
                || (Acesso.ehSuperAdmin(ator) && (idCdParam == null || idCdParam.trim().isEmpty()))
                || tempMaxParam == null || tempMinParam == null) {
            response.sendRedirect("frigorifico?acao=novo&erro=validacao");
            return;
        }

        try {
            int idCd;
            if (Acesso.ehSuperAdmin(ator)) {
                idCd = Integer.parseInt(idCdParam);
                if (cdDao.buscarPorId(idCd) == null) {
                    response.sendRedirect("frigorifico?acao=novo&erro=validacao");
                    return;
                }
            } else {
                // Admin não escolhe uma CD diferente no request.
                idCd = ator.getIdCd();
            }
            Double tempMax = Double.parseDouble(tempMaxParam);
            Double tempMin = Double.parseDouble(tempMinParam);

            if (idParam == null || idParam.isEmpty()) {
                Frigorifico novo = new Frigorifico(nome, localizacao, idCd, tempMax, tempMin);
                frigorificoDao.inserir(novo);
            } else {
                int id = Integer.parseInt(idParam);
                Frigorifico atualizado = new Frigorifico(id, nome, localizacao, idCd, tempMax, tempMin);
                if (Acesso.ehSuperAdmin(ator)) {
                    frigorificoDao.atualizar(atualizado);
                } else {
                    if (frigorificoDao.buscarPorIdECd(id, idCd) == null) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Frigorífico fora da sua CD.");
                        return;
                    }
                    frigorificoDao.atualizarPorCd(atualizado, idCd);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro na conversão de números no formulário.");
        }

        response.sendRedirect("frigorifico?acao=listar");
    }
}
