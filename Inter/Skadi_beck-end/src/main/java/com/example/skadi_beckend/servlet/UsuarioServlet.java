package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.CDDAO;
import com.example.skadi_beckend.dao.UsuarioDAO;
import com.example.skadi_beckend.model.CD;
import com.example.skadi_beckend.model.Usuario;
import com.example.skadi_beckend.util.Validator; // <-- IMPORTAÇÃO DA SUA CLASSE

import com.example.skadi_beckend.dao.ExclusaoEmCascata;
import com.example.skadi_beckend.util.Acesso;
import com.example.skadi_beckend.util.Resposta;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.List;

/**
 * Servlet responsável por gerenciar as requisições HTTP da entidade Usuario.
 */
@WebServlet("/usuario")
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDao;
    private CDDAO cdDao;



    @Override
    public void init() {
        this.usuarioDao = new UsuarioDAO();
        this.cdDao = new CDDAO();
    }

    // Metodo de leitura e navegacao (listar, carregar formulario, excluir)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");
        if (acao == null) acao = "listar";

        try {
            switch (acao) {
                case "novo":
                    abrirFormularioNovo(request, response);
                    break;
                case "editar":
                    abrirFormularioEdicao(request, response);
                    break;
                case "buscarPorNome": // Requisito 8
                    buscarPorNome(request, response);
                    break;
                case "listar":
                default:
                    listarUsuarios(request, response);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Erro no GET (Usuario): " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar a solicitação.");
        }
    }

    // Metodo enviar dados do formulario (salvar / atualizar)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");

        if ("salvar".equals(acao)) {
            salvarUsuario(request, response);
        } else if ("excluir".equals(acao)) {
            excluirUsuario(request, response);
        } else {
            response.sendRedirect("usuario?acao=listar");
        }
    }


//    Metodos auxiliares

//   Listar usuario
    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Usuario> lista = Acesso.daUnidade(request, usuarioDao.listarUsuariosComNomeCD(), Usuario::getIdCd);
        request.setAttribute("listaUsuarios", lista);
        request.getRequestDispatcher("/lista-usuario.jsp").forward(request, response);
    }

//   Buscar por nome
    private void buscarPorNome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String trecho = request.getParameter("buscaNome");
        List<Usuario> lista;

        if (trecho != null && !trecho.trim().isEmpty()) {
            lista = Acesso.daUnidade(request, usuarioDao.buscarPorNome(trecho), Usuario::getIdCd);
        } else {
            lista = Acesso.daUnidade(request, usuarioDao.listarUsuariosComNomeCD(), Usuario::getIdCd);
        }

        request.setAttribute("listaUsuarios", lista);
        request.getRequestDispatcher("/lista-usuario.jsp").forward(request, response);
    }

    private void carregarDependenciasFormulario(HttpServletRequest request) {
        List<CD> listaCds = Acesso.cdsPermitidos(request);
        // o gestor escolhido tem que ser de uma unidade que a pessoa pode acessar
        List<Usuario> listaGestores = new java.util.ArrayList<>();
        for (Usuario u : usuarioDao.listarTodos()) {
            if (Acesso.cdPermitido(request, u.getIdCd())) {
                listaGestores.add(u);
            }
        }
        request.setAttribute("listaCds", listaCds);
        request.setAttribute("listaGestores", listaGestores);
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        carregarDependenciasFormulario(request);
        request.getRequestDispatcher("/form-usuario.jsp").forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Usuario usuario = usuarioDao.buscarPorId(id);

            carregarDependenciasFormulario(request);
            request.setAttribute("usuario", usuario);

            request.getRequestDispatcher("/form-usuario.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("usuario?acao=listar");
        }
    }

//   Excluir (de verdade, junto com o que estiver ligado a ele)
    private void excluirUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean apagou = false;
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            apagou = ExclusaoEmCascata.usuario(id);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido fornecido para exclusão.");
        }
        Resposta.lista(response, "usuario", apagou ? null : "exclusao");
    }

    //    Salvar
    private void salvarUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        String username = request.getParameter("username");
        String nome = request.getParameter("nome");
        String cpf = request.getParameter("cpf");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String cargo = request.getParameter("cargo");
        String nivelAcesso = request.getParameter("nivelAcesso");
        String idCdParam = request.getParameter("idCd");
        String codGestorParam = request.getParameter("codGestor");

//       Remove espaços vazios
        if (cpf != null) cpf = cpf.trim();
        if (email != null) email = email.trim();
        if (username != null) username = username.trim();
        if (nome != null) nome = nome.trim();

        if (username == null || username.isEmpty() || nome == null || nome.isEmpty() ||
                idCdParam == null ||
                !Validator.validarFormatoCpf(cpf) ||
                !Validator.validarEmail(email)) {

            System.out.println("Validação falhou");
            Resposta.erroFormulario(response, "usuario", idParam, "validacao");
            return;
        }

//      Valida a senha
        if ((idParam == null || idParam.isEmpty()) && !Validator.validarSenha(senha)) {
            System.out.println("Validação falhou (Senha Fraca)");
            Resposta.erroFormulario(response, "usuario", idParam, "senha_fraca");
            return;
        }

        // LIMPA A PONTUAÇÃO DO CPF ANTES DE SALVAR NO BANCO
        cpf = cpf.replaceAll("\\D", "");

        try {
            int idCd = Integer.parseInt(idCdParam);
            Integer codGestor = (codGestorParam != null && !codGestorParam.isEmpty()) ? Integer.parseInt(codGestorParam) : null;

            if (idParam == null || idParam.isEmpty()) {
//             Criptografa a senha do formulário antes de salvar
                String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt());

//              Passa a senhaCriptografada para o objeto ao invés da senha limpa
                Usuario novo = new Usuario(username, codGestor, nome, cpf, email, senhaCriptografada, cargo, nivelAcesso, idCd);
                if (!usuarioDao.inserir(novo)) {
                    Resposta.erroFormulario(response, "usuario", idParam, "banco");
                    return;
                }
            } else {
                int id = Integer.parseInt(idParam);
                Usuario atualizado = new Usuario(id, username, codGestor, nome, cpf, email, null, cargo, nivelAcesso, idCd);
                if (!usuarioDao.atualizar(atualizado)) {
                    Resposta.erroFormulario(response, "usuario", idParam, "banco");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro na conversão de números no formulário.");
        }

        response.sendRedirect("usuario?acao=listar");
    }
}