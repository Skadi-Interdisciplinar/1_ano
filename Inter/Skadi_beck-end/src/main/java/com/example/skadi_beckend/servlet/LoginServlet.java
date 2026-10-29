package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.UsuarioDAO;
import com.example.skadi_beckend.model.Usuario;
import com.example.skadi_beckend.util.Acesso;
import com.example.skadi_beckend.util.Validator;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

/*
* Servlet utilizado para Login
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private UsuarioDAO usuarioDao;

    @Override
    public void init() {
        this.usuarioDao = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("sair".equals(acao)) {
            HttpSession sessao = request.getSession(false);
            if (sessao != null) {
                sessao.invalidate();
            }
            response.sendRedirect("login-cliente.jsp?sucesso=saiu");
            return;
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    // Processa os dados enviados pelo login
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginParam = request.getParameter("login"); // Email, CPF ou Username
        String senhaParam = request.getParameter("senha");
        // "area" vem do formulário: cliente (padrão) ou sistema (login da equipe Skadi)
        boolean areaSistema = "sistema".equals(request.getParameter("area"));
        String paginaLogin = areaSistema ? "login-super-admin.jsp" : "login-cliente.jsp";

        if (loginParam == null || loginParam.trim().isEmpty() || senhaParam == null || senhaParam.isEmpty()) {
            response.sendRedirect(paginaLogin + "?erro=credenciais");
            return;
        }
        loginParam = loginParam.trim();

        if (Validator.validarFormatoCpf(loginParam)) {
            loginParam = loginParam.replaceAll("\\D", "");
        }

        Usuario usuarioAutenticado = usuarioDao.autenticar(loginParam, senhaParam);

        // A área do sistema é só da equipe Skadi; o login de cliente não serve para ela (e vice-versa não é exigido)
        if (usuarioAutenticado != null && areaSistema && !Acesso.ehSuperAdmin(usuarioAutenticado)) {
            usuarioAutenticado = null;
        }

        if (usuarioAutenticado != null) {
//           Cria sessao nova (evita reaproveitar um identificador de sessão antigo)
            HttpSession antiga = request.getSession(false);
            if (antiga != null) {
                antiga.invalidate();
            }
            HttpSession sessao = request.getSession(true);
            sessao.setAttribute(Acesso.USUARIO, usuarioAutenticado);
            sessao.setMaxInactiveInterval(60 * 60 * 4);

//          Lembre-se de mim
            String lembrar = request.getParameter("lembrar"); // Pega o valor do check box

            if (lembrar != null) {
                Cookie cookieEmail = new Cookie("lembrarEmail", loginParam);
                cookieEmail.setMaxAge(60 * 60 * 24 * 180);
                cookieEmail.setPath("/");
                cookieEmail.setHttpOnly(true);
                response.addCookie(cookieEmail);
            }
            else {
                // Apaga o cookie se o utilizador não marcar a caixa
                Cookie cookieEmail = new Cookie("lembrarEmail", "");
                cookieEmail.setMaxAge(0);
                cookieEmail.setPath("/");
                response.addCookie(cookieEmail);
            }

//          Operador só resolve avisos: não tem CRUD para abrir
            if ("operador".equals(usuarioAutenticado.getNivelAcesso())) {
                response.sendRedirect("acesso-negado.jsp");
                return;
            }

//          Manda para a pagina
            response.sendRedirect("cd?acao=listar");
        } else {
//           Não achou o nome ou a senha
            response.sendRedirect(paginaLogin + "?erro=credenciais");
        }
    }
}
