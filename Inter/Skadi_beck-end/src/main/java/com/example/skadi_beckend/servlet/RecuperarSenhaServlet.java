package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.UsuarioDAO;
import com.example.skadi_beckend.util.EmailUtil;
import com.example.skadi_beckend.util.Validator;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servlet utilizado para Recuperar senha (3 passos):
 *  1) gerarCodigo : a pessoa informa o e-mail e recebe um código de 6 dígitos;
 *  2) validarCodigo: confere o código;
 *  3) redefinir   : grava a nova senha (criptografada).
 * O e-mail fica na sessão (não aparece na URL) e o código vale 15 minutos, com 5 tentativas.
 */
@WebServlet("/recuperar-senha")
public class RecuperarSenhaServlet extends HttpServlet {

    private static final long VALIDADE_MS = 15 * 60 * 1000L;
    private static final long REENVIO_MS = 30 * 1000L;
    private static final int MAX_TENTATIVAS = 5;

    /** Código pendente de cada e-mail. */
    private static final class Pendente {
        final String codigo;
        final long criadoEm = System.currentTimeMillis();
        int tentativas = 0;

        Pendente(String codigo) {
            this.codigo = codigo;
        }

        boolean expirado() {
            return System.currentTimeMillis() - criadoEm > VALIDADE_MS;
        }
    }

    private static final Map<String, Pendente> PENDENTES = new ConcurrentHashMap<>();
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private UsuarioDAO usuarioDao;

    @Override
    public void init() {
        this.usuarioDao = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect("esqueci-senha.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("gerarCodigo".equals(acao)) {
            gerarCodigo(request, response);
        } else if ("validarCodigo".equals(acao)) {
            validarCodigo(request, response);
        } else if ("redefinir".equals(acao)) {
            redefinirSenha(request, response);
        } else {
            response.sendRedirect("login-cliente.jsp");
        }
    }

//  Passo 1: gera e envia o código
    private void gerarCodigo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = limpar(request.getParameter("email")).toLowerCase();

        if (!Validator.validarEmail(email)) {
            response.sendRedirect("esqueci-senha.jsp?erro=emailInvalido");
            return;
        }

        if (!usuarioDao.verificarEmailExiste(email)) {
            System.out.println("Tentativa de recuperação falhou: E-mail não cadastrado no banco.");
            response.sendRedirect("esqueci-senha.jsp?erro=naoEncontrado");
            return;
        }

//      Evita mandar vários e-mails seguidos para a mesma pessoa
        Pendente atual = PENDENTES.get(email);
        if (atual != null && !atual.expirado() && System.currentTimeMillis() - atual.criadoEm < REENVIO_MS) {
            request.getSession().setAttribute("recuperarEmail", email);
            response.sendRedirect("esqueci-codigo.jsp?erro=aguarde");
            return;
        }

        String codigo = String.format("%06d", ALEATORIO.nextInt(1_000_000));

        try {
            EmailUtil.enviarCodigoRecuperacao(email, codigo);
        } catch (Exception e) {
            System.out.println("Erro ao enviar o e-mail: " + e.getMessage());
            response.sendRedirect("esqueci-senha.jsp?erro=email");
            return;
        }

        PENDENTES.put(email, new Pendente(codigo));
        HttpSession sessao = request.getSession();
        sessao.setAttribute("recuperarEmail", email);
        sessao.removeAttribute("recuperarCodigoOk");
        response.sendRedirect("esqueci-codigo.jsp");
    }

//  Passo 2: confere o código
    private void validarCodigo(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession sessao = request.getSession();
        String email = (String) sessao.getAttribute("recuperarEmail");
        if (email == null) {
            response.sendRedirect("esqueci-senha.jsp");
            return;
        }

        String erro = conferirCodigo(email, limpar(request.getParameter("codigo")));
        if (erro != null) {
            response.sendRedirect("esqueci-codigo.jsp?erro=" + erro);
            return;
        }

        sessao.setAttribute("recuperarCodigoOk", Boolean.TRUE);
        response.sendRedirect("redefinir-senha.jsp");
    }

//  Passo 3: nova senha
    private void redefinirSenha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession sessao = request.getSession();
        String email = (String) sessao.getAttribute("recuperarEmail");
        if (email == null) {
            response.sendRedirect("esqueci-senha.jsp");
            return;
        }

//      Confere o código de novo (vem junto no formulário, ou foi validado no passo 2)
        String codigoDigitado = limpar(request.getParameter("codigo"));
        if (!codigoDigitado.isEmpty() || !Boolean.TRUE.equals(sessao.getAttribute("recuperarCodigoOk"))) {
            String erro = conferirCodigo(email, codigoDigitado);
            if (erro != null) {
                response.sendRedirect("esqueci-codigo.jsp?erro=" + erro);
                return;
            }
        }

        String novaSenha = request.getParameter("novaSenha");
        String confirmar = request.getParameter("confirmarSenha");

        if (!Validator.validarSenha(novaSenha)) {
            response.sendRedirect("redefinir-senha.jsp?erro=senhaInvalida");
            return;
        }
        if (confirmar != null && !confirmar.equals(novaSenha)) {
            response.sendRedirect("redefinir-senha.jsp?erro=senhasDiferentes");
            return;
        }

//      Criptografa e salva
        String novaSenhaHash = BCrypt.hashpw(novaSenha, BCrypt.gensalt());
        if (usuarioDao.atualizarSenha(email, novaSenhaHash)) {
            PENDENTES.remove(email);
            sessao.removeAttribute("recuperarEmail");
            sessao.removeAttribute("recuperarCodigoOk");
            response.sendRedirect("login-cliente.jsp?sucesso=senhaRedefinida");
        } else {
            response.sendRedirect("redefinir-senha.jsp?erro=banco");
        }
    }

    /** Devolve null se o código está certo; senão o nome do erro. Um código vencido é descartado. */
    private String conferirCodigo(String email, String codigoDigitado) {
        Pendente pendente = PENDENTES.get(email);
        if (pendente == null) {
            return "codigoExpirado";
        }
        if (pendente.expirado()) {
            PENDENTES.remove(email, pendente);
            return "codigoExpirado";
        }
        if (pendente.tentativas >= MAX_TENTATIVAS) {
            PENDENTES.remove(email, pendente);
            return "tentativas";
        }
        if (!pendente.codigo.equals(codigoDigitado)) {
            pendente.tentativas++;
            return "codigoInvalido";
        }
        return null;
    }

    private String limpar(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
