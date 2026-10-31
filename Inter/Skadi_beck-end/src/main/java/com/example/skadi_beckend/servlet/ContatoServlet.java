package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.util.EmailUtil;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Endpoint para receber mensagens de contato do site
 */
@WebServlet("/contato")
public class ContatoServlet extends HttpServlet {

    private static final int LIMITE_NOME = 150;
    private static final int LIMITE_EMPRESA = 150;
    private static final int LIMITE_MENSAGEM = 5000;

    // GET não envia mensagens, endpoint recebe os dados por POST
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        responder(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "{" +
                        "\"sucesso\":false,\"mensagem\":\"Use POST para enviar uma mensagem.\"" +
                        "}");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//      Define UTF-8 antes de ler os campos para preservar acentos e caracteres especiais
        request.setCharacterEncoding("UTF-8");

        // Lê e remove espaços extras dos dados enviados pelo formulário
        String nome = limpar(request.getParameter("nomeCompleto"));
        String email = limpar(request.getParameter("email"));
        String empresa = limpar(request.getParameter("nomeEmpresa"));
        String mensagem = limpar(request.getParameter("mensagem"));

        // Impede o envio de campos vazios, grandes demais ou com e-mail inválido
        if (nome.isEmpty() || email.isEmpty() || empresa.isEmpty() || mensagem.isEmpty()
                || nome.length() > LIMITE_NOME
                || email.length() > 254
                || empresa.length() > LIMITE_EMPRESA
                || mensagem.length() > LIMITE_MENSAGEM
                || !emailValido(email)) {
            responder(response, HttpServletResponse.SC_BAD_REQUEST,
                    "{\"sucesso\":false,\"mensagem\":\"Confira os campos e tente novamente.\"}");
            return;
        }

        // Envia os dados para o e-mail configurado, o e-mail informado pode ser usado para responder
        if (EmailUtil.enviarContato(nome, email, empresa, mensagem)) {
            responder(response, HttpServletResponse.SC_OK,
                    "{\"sucesso\":true,\"mensagem\":\"Mensagem enviada com sucesso.\"}");
        } else {
            responder(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "{\"sucesso\":false,\"mensagem\":\"Não foi possível enviar sua mensagem agora.\"}");
        }
    }

    private static String limpar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    // Confere se o valor informado tem formato de endereço de e-mail
    private static boolean emailValido(String email) {
        try {
            InternetAddress endereco = new InternetAddress(email, true);
            endereco.validate();
            return true;
        } catch (AddressException e) {
            return false;
        }
    }

    private static void responder(HttpServletResponse response, int status, String json) throws IOException {
        // Retorna o resultado como JSON para o cliente que chamou o endpoint
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        response.getWriter().write(json);
    }
}
