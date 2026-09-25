package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.FrigorificoDAO;
import com.example.skadi_beckend.dao.CDDAO;
import com.example.skadi_beckend.model.Frigorifico;
import com.example.skadi_beckend.model.CD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;


@WebServlet("/frigorifico")
public class FrigorificoServlet extends HttpServlet {

    private FrigorificoDAO frigorificoDAO;
    private CDDAO cdDAO;

    @Override
    public void init() throws ServletException {
        this.frigorificoDAO = new FrigorificoDAO();
        this.cdDAO = new CDDAO();
    }

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
                frigorificoDAO.deletar(id);
                // Redireciona para a nova rota
                response.sendRedirect("frigorifico?action=listar");

            } else if (action.equals("novo") || action.equals("editar")) {
                // Busca a lista de CDs no banco para o menu de seleção
                List<CD> listaCDs = cdDAO.listarTodos();
                request.setAttribute("listaCDs", listaCDs);

                if (action.equals("editar")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Frigorifico f = frigorificoDAO.buscarPorId(id);
                    request.setAttribute("frigorificoParaEditar", f);
                }

                // Quando o html estiver pronto mudar o nome certinho
                request.getRequestDispatcher("cadastroFrigorifico.jsp").forward(request, response);

            } else { // "listar"
                List<Frigorifico> lista = frigorificoDAO.listarTodos();
                request.setAttribute("listaFrigorificos", lista);

                // Quando o html estiver pronto mudar o nome certinho
                request.getRequestDispatcher("listaFrigorificos.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("Erro no banco de dados: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Recebe os dados do HTML
        String idStr = request.getParameter("id_frigorifico");
        String nome = request.getParameter("nome");
        String localizacao = request.getParameter("localizacao");

        // Converter texto para números
        double tempMin = Double.parseDouble(request.getParameter("temperatura_min"));
        double tempMax = Double.parseDouble(request.getParameter("temperatura_max"));
        int idCd = Integer.parseInt(request.getParameter("id_cd"));

        // Monta o objeto Frigorifico
        Frigorifico frigorifico = new Frigorifico();
        frigorifico.setNome(nome);
        frigorifico.setLocalizacao(localizacao);
        frigorifico.setTemperatura_min(tempMin);
        frigorifico.setTemperatura_max(tempMax);
        frigorifico.setId_cd(idCd);

        try {
            if (idStr == null || idStr.isEmpty()) {
                // Se não tem ID é um cadastro novo
                frigorificoDAO.inserir(frigorifico);
            } else {
                // Se já tem ID é uma edição
                frigorifico.setId_frigorifico(Integer.parseInt(idStr));
                frigorificoDAO.atualizar(frigorifico);
            }

            // Após salvar, volta para a tela de lista
            response.sendRedirect("frigorifico?action=listar");

        } catch (Exception e) {
            throw new ServletException("Erro ao salvar Frigorifico: " + e.getMessage());
        }
    }
}