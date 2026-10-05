package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.LeituraTemperaturaDAO;
import com.example.skadi_beckend.dao.TermometroDAO;
import com.example.skadi_beckend.model.LeituraTemperatura;
import com.example.skadi_beckend.model.Termometro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/leituratemperatura")
public class LeituraTemperaturaServlet extends HttpServlet {

    private LeituraTemperaturaDAO leituraDAO;
    private TermometroDAO termometroDAO;

    @Override
    public void init() throws ServletException {
        this.leituraDAO = new LeituraTemperaturaDAO();
        this.termometroDAO = new TermometroDAO();
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
                leituraDAO.deletar(id);
                response.sendRedirect("leituratemperatura?action=listar");

            } else if (action.equals("novo") || action.equals("editar")) {
                List<Termometro> listaTermometros = termometroDAO.listarTodos();
                request.setAttribute("listaTermometros", listaTermometros);

                if (action.equals("editar")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    LeituraTemperatura leitura = leituraDAO.buscarPorId(id);
                    request.setAttribute("leituraParaEditar", leitura);
                }

                request.getRequestDispatcher("cadastroLeitura.jsp").forward(request, response);

            } else {
                List<LeituraTemperatura> lista = leituraDAO.listarTodos();
                request.setAttribute("listaLeituras", lista);
                request.getRequestDispatcher("listaLeituras.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("Erro no banco de dados: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id_leitura");
        double temperatura = Double.parseDouble(request.getParameter("temperatura"));

        // Pega a data ou usa a data atual caso venha vazio
        String dataStr = request.getParameter("data");
        Date data;
        if (dataStr == null || dataStr.isEmpty()) {
            data = new Date(System.currentTimeMillis());
        } else {
            data = Date.valueOf(dataStr);
        }

        int idTermometro = Integer.parseInt(request.getParameter("id_termometro"));

        LeituraTemperatura leitura = new LeituraTemperatura();
        leitura.setTemperatura(temperatura);
        leitura.setData(data);
        leitura.setId_termometro(idTermometro);

        try {
            if (idStr == null || idStr.isEmpty()) {
                leituraDAO.inserir(leitura);
            } else {
                leitura.setId_leitura(Integer.parseInt(idStr));
                leituraDAO.atualizar(leitura);
            }

            response.sendRedirect("leituratemperatura?action=listar");

        } catch (Exception e) {
            throw new ServletException("Erro ao salvar Leitura: " + e.getMessage());
        }
    }
}