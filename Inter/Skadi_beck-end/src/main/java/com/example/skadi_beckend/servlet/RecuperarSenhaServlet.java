package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.UsuarioDAO;
import com.example.skadi_beckend.util.Validator;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Servlet utilizado para Recuperar senha e Redefinir nova senha
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

        if ("gerarCodigo".equals(acao)) {gerarCodigo(request, response);
        } else if ("redefinir".equals(acao)) {
            redefinirSenha(request, response);
        } else {
            response.sendRedirect("login.jsp");
        }
    }

//  Gera o código
    private void gerarCodigo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");

        if (!usuarioDao.verificarEmailExiste(email)) {
            System.out.println("Tentativa de recuperação falhou: E-mail não cadastrado no banco.");
            response.sendRedirect("esqueci-senha.jsp?erro=naoEncontrado");
            return;
        }

        String codigo = String.format("%06d", new Random().nextInt(999999));
        tokens.put(email, codigo);

        try {
            System.out.println("E-mail enviado com sucesso para: " + email);
            com.example.skadi_beckend.util.EmailUtil.enviarCodigoRecuperacao(email, codigo);
            response.sendRedirect("redefinir-senha.jsp?email=" + email);
        } catch (Exception e) {
            System.out.println("Erro ao enviar o e-mail: " + e.getMessage());
            response.sendRedirect("esqueci-senha.jsp?erro=email");
        }
    }

//  Redefinir senha
    private void redefinirSenha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String codigoDigitado = request.getParameter("codigo");
        String novaSenha = request.getParameter("novaSenha");

        String codigoSalvo = tokens.get(email);

//      Verifica o se o codigo é o mesmo
        if (codigoSalvo == null || !codigoSalvo.equals(codigoDigitado)) {
            response.sendRedirect("redefinir-senha.jsp?email=" + email + "&erro=codigoInvalido");
            return;
        }

//        Valida de a senha tem 8 letras
        if (!Validator.validarSenha(novaSenha)) {
            response.sendRedirect("redefinir-senha.jsp?email=" + email + "&erro=senhaInvalida");
            return;
        }

//      Criptografa e salva
        String novaSenhaHash = BCrypt.hashpw(novaSenha, BCrypt.gensalt());
        boolean atualizou = usuarioDao.atualizarSenha(email, novaSenhaHash);

        if (atualizou) {
            tokens.remove(email); // Limpa o token usado
            response.sendRedirect("login.jsp?sucesso=senhaRedefinida");
        } else {
            response.sendRedirect("redefinir-senha.jsp?email=" + email + "&erro=banco");
        }
    }
}