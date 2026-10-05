package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.TermometroDAO;
import com.example.skadi_beckend.dao.FrigorificoDAO;
import com.example.skadi_beckend.model.Termometro;
import com.example.skadi_beckend.model.Frigorifico;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/termometro")
public class TermometroServlet extends HttpServlet {

    private TermometroDAO termometroDAO;
    private FrigorificoDAO refrigeradorDAO;

    @Override
    public void init() throws ServletException {
        this.termometroDAO = new TermometroDAO();
        this.refrigeradorDAO = new FrigorificoDAO();
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
                termometroDAO.deletar(id);
                response.sendRedirect("termometro?action=listar");

            } else if (action.equals("novo") || action.equals("editar")) {

                List<Frigorifico> listaRefrigeradores = refrigeradorDAO.listarTodos();
                request.setAttribute("listaRefrigeradores", listaRefrigeradores);

                if (action.equals("editar")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Termometro termometro = termometroDAO.buscarPorId(id);
                    request.setAttribute("termometroParaEditar", termometro);
                }

                request.getRequestDispatcher("cadastroTermometro.jsp").forward(request, response);

            } else {
                List<Termometro> lista = termometroDAO.listarTodos();
                request.setAttribute("listaTermometros", lista);
                request.getRequestDispatcher("listaTermometros.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("Erro no banco de dados: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id_termometro");
        String modelo = request.getParameter("modelo");
        String status = request.getParameter("status");


        if (status == null || status.isEmpty()) {
            status = "ativo";
        }

        int idRefrigerador = Integer.parseInt(request.getParameter("id_refrigerador"));

        Termometro termometro = new Termometro();
        termometro.setModelo(modelo);
        termometro.setStatus(status);
        termometro.setId_refrigerador(idRefrigerador);

        try {
            if (idStr == null || idStr.isEmpty()) {
                termometroDAO.inserir(termometro);
            } else {
                termometro.setId_termometro(Integer.parseInt(idStr));
                termometroDAO.atualizar(termometro);
            }

            response.sendRedirect("termometro?action=listar");

        } catch (Exception e) {
            throw new ServletException("Erro ao salvar Termometro: " + e.getMessage());
        }
    }
}