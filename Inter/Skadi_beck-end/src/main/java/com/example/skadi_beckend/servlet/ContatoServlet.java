package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.util.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet da página "Contate-nos": recebe nome, sobrenome, empresa e mensagem e manda por e-mail para a equipe Skadi.
 * (Cadastro de novos clientes é por aprovação manual: a pessoa fala com a equipe.)
 */
@WebServlet("/contato")
public class ContatoServlet extends HttpServlet {

    private static final int MAX_NOME = 100;
    private static final int MAX_MENSAGEM = 2000;
    private static final long INTERVALO_MS = 30_000;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("contato.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = limpar(request.getParameter("nome"));
        String sobrenome = limpar(request.getParameter("sobrenome"));
        String empresa = limpar(request.getParameter("empresa"));
        String mensagem = limpar(request.getParameter("mensagem"));

        if (nome.isEmpty() || sobrenome.isEmpty() || empresa.isEmpty()
                || nome.length() > MAX_NOME || sobrenome.length() > MAX_NOME || empresa.length() > MAX_NOME
                || mensagem.length() < 10 || mensagem.length() > MAX_MENSAGEM) {
            response.sendRedirect("contato.jsp?erro=validacao");
            return;
        }

        // Freio simples contra envio em sequência (a página é pública)
        HttpSession sessao = request.getSession();
        Long ultimo = (Long) sessao.getAttribute("contatoEnviadoEm");
        if (ultimo != null && System.currentTimeMillis() - ultimo < INTERVALO_MS) {
            response.sendRedirect("contato.jsp?erro=aguarde");
            return;
        }

        try {
            EmailUtil.enviarContato(nome, sobrenome, empresa, mensagem);
            sessao.setAttribute("contatoEnviadoEm", System.currentTimeMillis());
            response.sendRedirect("contato.jsp?enviado=1");
        } catch (Exception e) {
            System.out.println("Erro ao enviar o contato: " + e.getMessage());
            response.sendRedirect("contato.jsp?erro=email");
        }
    }

    private String limpar(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
