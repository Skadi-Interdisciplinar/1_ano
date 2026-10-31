package com.example.skadi_beckend.util;

import com.example.skadi_beckend.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Locale;

public final class Acesso {

    public static final String SESSAO_USUARIO = "usuarioLogado";

    private Acesso() {
    }

    // Recupera da sessão a conta que já passou pelo login
    public static Usuario usuario(HttpServletRequest request) {
        Object usuario = request.getSession(false) == null
                ? null : request.getSession(false).getAttribute(SESSAO_USUARIO);
        return usuario instanceof Usuario ? (Usuario) usuario : null;
    }

    public static boolean ehSuperAdmin(Usuario usuario) {
        return usuario != null && nivel(usuario).equals("super_admin");
    }

    public static boolean ehAdmin(Usuario usuario) {
        return usuario != null && nivel(usuario).equals("admin");
    }

    // Gestores e operadores são cadastros da CD e não podem iniciar sessão
    public static boolean podeEntrar(Usuario usuario) {
        return ehAdmin(usuario) || ehSuperAdmin(usuario);
    }

    // Cada servlet protegido chama esta validação
    public static boolean exigirAdminOuSuperAdmin(HttpServletRequest request,
                                                   HttpServletResponse response) throws IOException {
        Usuario usuario = usuario(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login-sistema.jsp?erro=sessao");
            return false;
        }
        if (!podeEntrar(usuario)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso restrito a administradores.");
            return false;
        }
        return true;
    }

//  Todas operações em SuperAdmin
    public static boolean exigirSuperAdmin(HttpServletRequest request,
                                           HttpServletResponse response) throws IOException {
        if (!exigirAdminOuSuperAdmin(request, response)) return false;
        if (!ehSuperAdmin(usuario(request))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Ação exclusiva do super_admin.");
            return false;
        }
        return true;
    }

    // super_admin acessa todas as CDs, admin só acessa a CD vinculada à sua conta
    public static boolean podeAcessarCd(Usuario usuario, int idCd) {
        return ehSuperAdmin(usuario)
                || (ehAdmin(usuario) && usuario.getIdCd() != null && usuario.getIdCd() == idCd);
    }

    // Admin precisa cadastrar sua CD, super_admin pode permanecer sem CD
    public static boolean exigirCdConfigurada(HttpServletRequest request,
                                               HttpServletResponse response) throws IOException {
        Usuario usuario = usuario(request);
        if (ehAdmin(usuario) && usuario.getIdCd() == null) {
            response.sendRedirect(request.getContextPath() + "/cd?acao=novo");
            return false;
        }
        return true;
    }

    // Pega nivel de acesso
    private static String nivel(Usuario usuario) {
        return usuario.getNivelAcesso() == null
                ? "" : usuario.getNivelAcesso().trim().toLowerCase(Locale.ROOT);
    }
}
