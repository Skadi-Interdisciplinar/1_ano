package com.example.skadi_beckend.servlet;

import com.google.gson.Gson;
import com.example.skadi_beckend.dao.CDDAO;
import com.example.skadi_beckend.model.CD;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet que funciona como uma API REST para a entidade CD.
 * Mapeado para a URL "/api/cd".
 */
@WebServlet("/api/cd")
public class CDServlet extends HttpServlet {
    private CDDAO cdDAO = new CDDAO();
    private Gson gson = new Gson();

    // GET: Retorna a lista de CDs em JSON
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        List<CD> listaCds = cdDAO.listarTodos();
        String json = gson.toJson(listaCds);

        resp.getWriter().write(json);
    }

    // POST: Recebe um JSON e insere no banco
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            // Lê o JSON enviado pelo JavaScript e converte para o objeto CD
            CD novoCd = gson.fromJson(req.getReader(), CD.class);

            boolean sucesso = cdDAO.inserir(novoCd);

            if (sucesso) {
                resp.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
                resp.getWriter().write("{\"mensagem\": \"CD cadastrado com sucesso!\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
                resp.getWriter().write("{\"erro\": \"Falha ao cadastrar no banco. Verifique se o CNPJ tem 14 dígitos e não é duplicado.\"}");
            }
        } catch (Exception e) {
            // Imprime o erro
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400

            // Devolve o erro exato para o navegador exibir na tela em vez do erro genérico
            resp.getWriter().write("{\"erro\": \"Erro interno do Java: " + e.getMessage() + "\"}");
        }
    }
}