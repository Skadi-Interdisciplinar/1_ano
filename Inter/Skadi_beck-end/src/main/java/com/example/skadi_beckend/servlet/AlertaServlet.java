package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.AlertaDAO;
import com.example.skadi_beckend.dao.LeituraTemperaturaDAO;
import com.example.skadi_beckend.dao.UsuarioDAO;
import com.example.skadi_beckend.model.Alerta;
import com.example.skadi_beckend.model.LeituraTemperatura;
import com.example.skadi_beckend.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/alerta")
public class AlertaServlet extends HttpServlet {

    private AlertaDAO alertaDAO;
    private LeituraTemperaturaDAO leituraDAO;
    private UsuarioDAO usuarioDAO;

    @Override
    public void init() throws ServletException {
        this.alertaDAO = new AlertaDAO();
        this.leituraDAO = new LeituraTemperaturaDAO();
        this.usuarioDAO = new UsuarioDAO();
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
                alertaDAO.deletar(id);
                response.sendRedirect("alerta?action=listar");

            } else if (action.equals("novo") || action.equals("editar")) {
                // Carrega leituras e usuarios para selecionar
                List<LeituraTemperatura> listaLeituras = leituraDAO.listarTodos();
                List<Usuario> listaUsuarios = usuarioDAO.listarTodos();

                request.setAttribute("listaLeituras", listaLeituras);
                request.setAttribute("listaUsuarios", listaUsuarios);

                if (action.equals("editar")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Alerta alerta = alertaDAO.buscarPorId(id);
                    request.setAttribute("alertaParaEditar", alerta);
                }

                request.getRequestDispatcher("cadastroAlerta.jsp").forward(request, response);

            } else {
                List<Alerta> lista = alertaDAO.listarTodos();
                request.setAttribute("listaAlertas", lista);
                request.getRequestDispatcher("listaAlertas.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("Erro no banco de dados: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id_alerta");
        String status = request.getParameter("status");
        if (status == null || status.isEmpty()) status = "pendente";

        String tempoSobrevivenciaStr = request.getParameter("tempo_sobrevivencia");
        Integer tempoSobrevivencia = (tempoSobrevivenciaStr != null && !tempoSobrevivenciaStr.isEmpty())
                ? Integer.parseInt(tempoSobrevivenciaStr) : null;

        String tipo = request.getParameter("tipo");
        String nivelGravidade = request.getParameter("nivel_gravidade");
        if (nivelGravidade == null || nivelGravidade.isEmpty()) nivelGravidade = "baixo";

        String canal = request.getParameter("canal");
        String notificacao = request.getParameter("notificacao");

        String dataEnvioStr = request.getParameter("data_envio");
        Date dataEnvio = (dataEnvioStr != null && !dataEnvioStr.isEmpty()) ? Date.valueOf(dataEnvioStr) : null;

        int idLeitura = Integer.parseInt(request.getParameter("id_leitura"));

        String idUsuarioStr = request.getParameter("id_usuario");
        Integer idUsuario = (idUsuarioStr != null && !idUsuarioStr.isEmpty()) ? Integer.parseInt(idUsuarioStr) : null;

        Alerta alerta = new Alerta();
        alerta.setStatus(status);
        alerta.setTempo_sobrevivencia(tempoSobrevivencia);
        alerta.setTipo(tipo);
        alerta.setNivel_gravidade(nivelGravidade);
        alerta.setCanal(canal);
        alerta.setNotificacao(notificacao);
        alerta.setData_envio(dataEnvio);
        alerta.setId_leitura(idLeitura);
        alerta.setId_usuario(idUsuario);

        try {
            if (idStr == null || idStr.isEmpty()) {
                alertaDAO.inserir(alerta);
            } else {
                alerta.setId_alerta(Integer.parseInt(idStr));
                alertaDAO.atualizar(alerta);
            }

            response.sendRedirect("alerta?action=listar");

        } catch (Exception e) {
            throw new ServletException("Erro ao salvar Alerta: " + e.getMessage());
        }
    }
}