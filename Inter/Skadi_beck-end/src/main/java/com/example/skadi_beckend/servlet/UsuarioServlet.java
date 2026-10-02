package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.UsuarioDAO;
import com.example.skadi_beckend.dao.CDDAO;
import com.example.skadi_beckend.model.Usuario;
import com.example.skadi_beckend.model.CD;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/usuario")
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private CDDAO cdDAO;

    @Override
    public void init() throws ServletException {
        this.usuarioDAO = new UsuarioDAO();
        this.cdDAO = new CDDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "listar";
        }

        try {
            if (action.equals("deletar")) {
                int id = Integer.parseInt(request.getParameter("id"));
                usuarioDAO.deletar(id);
                response.sendRedirect("usuario?action=listar");

            } else if (action.equals("novo") || action.equals("editar")) {
                // Busca CDs para o dropdown na tela
                List<CD> listaCDs = cdDAO.listarTodos();
                request.setAttribute("listaCDs", listaCDs);

                if (action.equals("editar")) {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Usuario usuario = usuarioDAO.buscarPorId(id);
                    request.setAttribute("usuarioParaEditar", usuario);
                }

                request.getRequestDispatcher("cadastroUsuario.jsp").forward(request, response);

            } else {
                List<Usuario> lista = usuarioDAO.listarTodos();
                request.setAttribute("listaUsuarios", lista);
                request.getRequestDispatcher("listaUsuarios.jsp").forward(request, response);
            }
        } catch (Exception e) {
            throw new ServletException("Erro no banco de dados: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id_usuario");
        String nome = request.getParameter("nome");
        String cpf = request.getParameter("cpf");
        String email = request.getParameter("email");
        String cargo = request.getParameter("cargo");
        String nivelAcesso = request.getParameter("nivel_acesso");
        int idCd = Integer.parseInt(request.getParameter("id_cd"));

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setEmail(email);
        usuario.setCargo(cargo);
        usuario.setNivel_acesso(nivelAcesso);
        usuario.setId_cd(idCd);

        try {
            if (idStr == null || idStr.isEmpty()) {
                usuarioDAO.inserir(usuario);
            } else {
                usuario.setId_usuario(Integer.parseInt(idStr));
                usuarioDAO.atualizar(usuario);
            }

            response.sendRedirect("usuario?action=listar");

        } catch (Exception e) {
            throw new ServletException("Erro ao salvar Usuario: " + e.getMessage());
        }
    }
}