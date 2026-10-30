package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.UsuarioDAO;
import com.example.skadi_beckend.model.Usuario;
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
            response.sendRedirect("login.jsp");
            return;
        }

        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    // Processa os dados enviados pelo login
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String loginParam = request.getParameter("login"); // Email, CPF ou Username
        String senhaParam = request.getParameter("senha");

        if (Validator.validarFormatoCpf(loginParam)) {
            loginParam = loginParam.replaceAll("\\D", "");
        }

        Usuario usuarioAutenticado = usuarioDao.autenticar(loginParam, senhaParam);

        if (usuarioAutenticado != null) {
//           Cria sessao
            HttpSession sessao = request.getSession();
            sessao.setAttribute("usuarioLogado", usuarioAutenticado);

//          Lembre-se de mim
            String lembrar = request.getParameter("lembrar"); // Pega o valor do check box

            if (lembrar != null) {

                Cookie cookieEmail = new Cookie("lembrarEmail", loginParam);
                cookieEmail.setMaxAge(60 * 60 * 24 * 180);
                cookieEmail.setPath("/");
                response.addCookie(cookieEmail);
            }
            else {
                // Apaga o cookie se o utilizador não marcar a caixa
                Cookie cookieEmail = new Cookie("lembrarEmail", "");
                cookieEmail.setMaxAge(0);
                cookieEmail.setPath("/");
                response.addCookie(cookieEmail);
            }

//          Manda para a pagina
            response.sendRedirect("cd?acao=listar");
        } else {
//           Não achou o nome ou a senha
            response.sendRedirect("login.jsp?erro=credenciais");
        }
    }
}