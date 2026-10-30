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
import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servlet utilizado para Recuperar senha e Redefinir nova senha
 */
@WebServlet("/recuperar-senha")
public class RecuperarSenhaServlet extends HttpServlet {

    private UsuarioDAO usuarioDao;
    // Guarda o código e o prazo de validade enquanto a recuperação está em andamento
    private static final Map<String, CodigoRecuperacao> tokens = new ConcurrentHashMap<>();
    private static final SecureRandom GERADOR_CODIGO = new SecureRandom();
    private static final long VALIDADE_CODIGO_MINUTOS = 10;

    @Override
    public void init() {
        this.usuarioDao = new UsuarioDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("gerarCodigo".equals(acao)) {
            gerarCodigo(request, response);
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

//      Remove códigos expirados e gera um código numérico de seis dígitos
        Instant agora = Instant.now();
        tokens.entrySet().removeIf(entrada -> !entrada.getValue().expiraEm.isAfter(agora));

        String codigo = String.format(Locale.ROOT, "%06d", GERADOR_CODIGO.nextInt(1_000_000));
        Instant expiraEm = agora.plus(VALIDADE_CODIGO_MINUTOS, ChronoUnit.MINUTES);
        CodigoRecuperacao codigoRecuperacao = new CodigoRecuperacao(codigo, expiraEm);
        tokens.put(email, codigoRecuperacao);

        try {
            boolean enviado = com.example.skadi_beckend.util.EmailUtil.enviarCodigoRecuperacao(email, codigo);
            if (!enviado) {
                tokens.remove(email, codigoRecuperacao);
                response.sendRedirect("esqueci-senha.jsp?erro=email");
                return;
            }

            System.out.println("E-mail enviado com sucesso para: " + email);
            response.sendRedirect("redefinir-senha.jsp?email=" + email);
        } catch (Exception e) {
            tokens.remove(email, codigoRecuperacao);
            System.out.println("Erro ao enviar o e-mail: " + e.getMessage());
            response.sendRedirect("esqueci-senha.jsp?erro=email");
        }
    }

//  Redefinir senha
    private void redefinirSenha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String codigoDigitado = request.getParameter("codigo");
        String novaSenha = request.getParameter("novaSenha");

        CodigoRecuperacao codigoSalvo = tokens.get(email);

//      Verifica o se o codigo é o mesmo
        if (codigoSalvo == null || !codigoSalvo.expiraEm.isAfter(Instant.now())
                || !codigoSalvo.codigo.equals(codigoDigitado)) {
            if (codigoSalvo != null && !codigoSalvo.expiraEm.isAfter(Instant.now())) {
                tokens.remove(email, codigoSalvo);
            }
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

    //   Representa o código temporário e o instante em que ele deixa de ser válido
    private static class CodigoRecuperacao {
        private final String codigo;
        private final Instant expiraEm;

        private CodigoRecuperacao(String codigo, Instant expiraEm) {
            this.codigo = codigo;
            this.expiraEm = expiraEm;
        }
    }
}
