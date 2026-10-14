package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.UsuarioDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
* Servlet utilizado para Recuperar senha
*/
@WebServlet("/recuperar-senha")
public class RecuperarSenhaServlet extends HttpServlet {

    private UsuarioDAO usuarioDao;

    private static Map<String, String> tokens = new HashMap<>();

    @Override
    public void init() {
        this.usuarioDao = new UsuarioDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("gerarCodigo".equals(acao)) {
            String email = request.getParameter("email");

//          Ve se o E-MAIL existe
            if (!usuarioDao.verificarEmailExiste(email)) {
                System.out.println("Tentativa de recuperação falhou: E-mail não cadastrado no banco.");
                response.sendRedirect("esqueci-senha.jsp?erro=naoEncontrado");

            }


//            Gera um código aleatório de 6 números
            String codigo = String.format("%06d", new Random().nextInt(999999));

//           Guarda o código atrelado ao e-mail
            tokens.put(email, codigo);

            // E-MAIL
            try {
                String assunto = "Recuperação de Senha - Sistema Skadi";
                String mensagem = "Olá!\n\nSeu código de recuperação é: " + codigo +
                        "\n\nDigite este código no sistema para criar uma nova senha. " +
                        "Se você não solicitou isso, apenas ignore este e-mail.";


                System.out.println("E-mail enviado com sucesso para: " + email);
                com.example.skadi_beckend.util.EmailUtil.enviarCodigoRecuperacao(email, codigo);

                // Manda para a tela de digitar o código
                response.sendRedirect("redefinir-senha.jsp?email=" + email);

            } catch (Exception e) {
                System.out.println("Erro ao enviar o e-mail: " + e.getMessage());
                response.sendRedirect("esqueci-senha.jsp?erro=email");
            }
        }
    }
}