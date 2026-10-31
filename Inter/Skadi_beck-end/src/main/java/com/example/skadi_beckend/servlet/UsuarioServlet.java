package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.CDDAO;
import com.example.skadi_beckend.dao.UsuarioDAO;
import com.example.skadi_beckend.model.CD;
import com.example.skadi_beckend.model.Usuario;
import com.example.skadi_beckend.util.Acesso;
import com.example.skadi_beckend.util.Validator; // <-- IMPORTAÇÃO DA SUA CLASSE

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
        // Admin só acessa a gestão de funcionários depois de cadastrar a própria CD.
        if (!Acesso.exigirAdminOuSuperAdmin(request, response)
                || !Acesso.exigirCdConfigurada(request, response)) return;
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
                case "excluir":
                    excluirUsuario(request, response);
                    break;
                case "excluirPorCpf": // Requisito 7
                    excluirPorCpf(request, response);
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
        // A autorização também é verificada no envio, sem depender da página aberta.
        if (!Acesso.exigirAdminOuSuperAdmin(request, response)
                || !Acesso.exigirCdConfigurada(request, response)) return;
        String acao = request.getParameter("acao");

        if ("salvar".equals(acao)) {
            salvarUsuario(request, response);
        } else {
            response.sendRedirect("usuario?acao=listar");
        }
    }


//    Metodos auxiliares

//   Listar usuario
    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario ator = Acesso.usuario(request);
        // Admin vê funcionários de sua CD; super_admin vê os registros de todas as CDs.
        List<Usuario> lista = Acesso.ehSuperAdmin(ator)
                ? usuarioDao.listarUsuariosComNomeCD()
                : usuarioDao.listarFuncionariosPorCd(ator.getIdCd(), null);
        request.setAttribute("listaUsuarios", lista);
        request.setAttribute("ehSuperAdmin", Acesso.ehSuperAdmin(ator));
        request.getRequestDispatcher("/lista-usuario.jsp").forward(request, response);
    }

//   Buscar por nome
    private void buscarPorNome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String trecho = request.getParameter("buscaNome");
        List<Usuario> lista;
        Usuario ator = Acesso.usuario(request);

        // A busca do admin continua limitada aos funcionários da própria CD.
        if (Acesso.ehSuperAdmin(ator) && trecho != null && !trecho.trim().isEmpty()) {
            lista = usuarioDao.buscarPorNome(trecho);
        } else if (Acesso.ehSuperAdmin(ator)) {
            lista = usuarioDao.listarUsuariosComNomeCD();
        } else {
            lista = usuarioDao.listarFuncionariosPorCd(ator.getIdCd(), trecho);
        }

        request.setAttribute("listaUsuarios", lista);
        request.setAttribute("ehSuperAdmin", Acesso.ehSuperAdmin(ator));
        request.getRequestDispatcher("/lista-usuario.jsp").forward(request, response);
    }

    private void carregarDependenciasFormulario(HttpServletRequest request, Usuario ator) {
        // Admin recebe apenas sua unidade; super_admin pode escolher entre todas.
        List<CD> listaCds = Acesso.ehSuperAdmin(ator)
                ? cdDao.listarTodos()
                : new java.util.ArrayList<>(java.util.Collections.singletonList(cdDao.buscarPorId(ator.getIdCd())));
        listaCds.removeIf(java.util.Objects::isNull);
        List<Usuario> listaGestores = Acesso.ehSuperAdmin(ator)
                ? usuarioDao.listarGestores()
                : usuarioDao.listarFuncionariosPorCd(ator.getIdCd(), null);
        listaGestores.removeIf(usuario -> !"gestor".equalsIgnoreCase(usuario.getNivelAcesso()));
        request.setAttribute("listaCds", listaCds);
        request.setAttribute("listaGestores", listaGestores);
        request.setAttribute("ehSuperAdmin", Acesso.ehSuperAdmin(ator));
        if (!Acesso.ehSuperAdmin(ator) && !listaCds.isEmpty()) {
            request.setAttribute("nomeCdAtual", listaCds.get(0).getNome());
        }
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        carregarDependenciasFormulario(request, Acesso.usuario(request));
        request.getRequestDispatcher("/form-usuario.jsp").forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Usuario ator = Acesso.usuario(request);
            // O funcionário precisa pertencer à CD do admin; super_admin não pode editar super_admin.
            Usuario usuario = Acesso.ehSuperAdmin(ator)
                    ? usuarioDao.buscarPorId(id)
                    : usuarioDao.buscarFuncionarioPorIdECd(id, ator.getIdCd());
            if (usuario == null || "super_admin".equalsIgnoreCase(usuario.getNivelAcesso())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Usuário fora da sua permissão.");
                return;
            }

            carregarDependenciasFormulario(request, ator);
            request.setAttribute("usuario", usuario);

            request.getRequestDispatcher("/form-usuario.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("usuario?acao=listar");
        }
    }

//    Excluir
    private void excluirUsuario(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Usuario ator = Acesso.usuario(request);
            // Admin exclui só funcionário local; super_admin não pode excluir outra conta super_admin.
            if (Acesso.ehSuperAdmin(ator)) {
                Usuario alvo = usuarioDao.buscarPorId(id);
                if (alvo != null && !"super_admin".equalsIgnoreCase(alvo.getNivelAcesso())) {
                    usuarioDao.deletar(id);
                }
            } else {
                usuarioDao.deletarFuncionario(id, ator.getIdCd());
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido fornecido para exclusão.");
        }
        response.sendRedirect("usuario?acao=listar");
    }

//   Excluir por CPF
    private void excluirPorCpf(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String cpf = request.getParameter("cpf");


        if (Validator.validarFormatoCpf(cpf)) {
            // Limpa a pontuação
            cpf = cpf.replaceAll("\\D", "");
            Usuario ator = Acesso.usuario(request);
            // Exclusão por CPF segue o mesmo limite de CD usado pela exclusão por id.
            if (Acesso.ehSuperAdmin(ator)) usuarioDao.deletarPorCpf(cpf);
            else usuarioDao.deletarFuncionarioPorCpf(cpf, ator.getIdCd());
        }
        response.sendRedirect("usuario?acao=listar");
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
        Usuario ator = Acesso.usuario(request);
        boolean superAdmin = Acesso.ehSuperAdmin(ator);
        boolean edicao = idParam != null && !idParam.trim().isEmpty();

        // Normaliza os campos antes de validar o perfil e os dados enviados.
        if (cpf != null) cpf = cpf.trim();
        if (email != null) email = email.trim();
        if (username != null) username = username.trim();
        if (nome != null) nome = nome.trim();
        if (nivelAcesso != null) nivelAcesso = nivelAcesso.trim().toLowerCase(java.util.Locale.ROOT);

        if (nome == null || nome.isEmpty() || !Validator.validarFormatoCpf(cpf)
                || !Validator.validarEmail(email)) {
            response.sendRedirect("usuario?acao=novo&erro=validacao");
            return;
        }

        // Remove a pontuação para manter o CPF no formato usado pelo banco.
        cpf = cpf.replaceAll("\\D", "");

        try {
            Usuario existente = null;
            if (edicao) {
                int id = Integer.parseInt(idParam);
                // Admin só altera funcionários da própria CD; super_admin pode consultar as demais contas.
                existente = superAdmin
                        ? usuarioDao.buscarPorId(id)
                        : usuarioDao.buscarFuncionarioPorIdECd(id, ator.getIdCd());
                if (existente == null || "super_admin".equalsIgnoreCase(existente.getNivelAcesso())) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Usuário fora da sua permissão.");
                    return;
                }
                // Edição de dados não promove nem rebaixa o nível de acesso da conta.
                nivelAcesso = existente.getNivelAcesso().toLowerCase(java.util.Locale.ROOT);
            } else if (!("admin".equals(nivelAcesso) || "gestor".equals(nivelAcesso)
                    || "operador".equals(nivelAcesso))) {
                response.sendRedirect("usuario?acao=novo&erro=validacao");
                return;
            }

            boolean contaAdmin = "admin".equals(nivelAcesso);
            // Somente super_admin pode criar contas de administrador.
            if (contaAdmin && !superAdmin) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "Somente o super_admin pode criar administradores.");
                return;
            }

            Integer idCd;
            if (!superAdmin) {
                // Admin não escolhe a CD pelo formulário; usa a unidade associada à sessão.
                idCd = ator.getIdCd();
            } else if (contaAdmin) {
                // A conta admin é criada antes da CD, que será cadastrada pelo próprio administrador.
                idCd = existente == null ? null : existente.getIdCd();
            } else if (idCdParam == null || idCdParam.trim().isEmpty()) {
                idCd = existente == null ? null : existente.getIdCd();
            } else {
                idCd = Integer.valueOf(idCdParam);
                if (cdDao.buscarPorId(idCd) == null) {
                    response.sendRedirect("usuario?acao=novo&erro=validacao");
                    return;
                }
            }

            if (!contaAdmin && idCd == null) {
                response.sendRedirect("usuario?acao=novo&erro=validacao");
                return;
            }

            if (contaAdmin && (username == null || username.isEmpty())) {
                response.sendRedirect("usuario?acao=novo&erro=validacao");
                return;
            }
            if (contaAdmin && !edicao && !Validator.validarSenha(senha)) {
                response.sendRedirect("usuario?acao=novo&erro=senha_fraca");
                return;
            }

            // O gestor selecionado também precisa ser funcionário da mesma CD.
            Integer codGestor = codGestorParam != null && !codGestorParam.trim().isEmpty()
                    ? Integer.valueOf(codGestorParam) : null;
            if (contaAdmin) codGestor = null;
            if (codGestor != null) {
                Usuario gestor = usuarioDao.buscarFuncionarioPorIdECd(codGestor, idCd);
                if (gestor == null || !"gestor".equalsIgnoreCase(gestor.getNivelAcesso())) {
                    response.sendRedirect("usuario?acao=novo&erro=validacao");
                    return;
                }
            }

            String senhaCriptografada = null;
            if (!contaAdmin) {
                // Gestores e operadores não recebem credenciais de login.
                username = null;
            } else if (!edicao) {
                // Só grava o hash da senha para uma conta admin nova.
                senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt());
            }

            if (!edicao) {
                Usuario novo = new Usuario(username, codGestor, nome, cpf, email,
                        senhaCriptografada, cargo, nivelAcesso, idCd);
                if (!usuarioDao.inserir(novo)) {
                    response.sendRedirect("usuario?acao=novo&erro=validacao");
                    return;
                }
            } else {
                Usuario atualizado = new Usuario(existente.getIdUsuario(), username, codGestor,
                        nome, cpf, email, null, cargo, nivelAcesso, idCd);
                boolean atualizou;
                if (contaAdmin) {
                    atualizou = usuarioDao.atualizar(atualizado);
                } else if (superAdmin) {
                    atualizou = usuarioDao.atualizarFuncionarioPorSuperAdmin(atualizado);
                } else {
                    atualizou = usuarioDao.atualizarFuncionario(atualizado);
                }
                if (!atualizou) {
                    response.sendRedirect("usuario?acao=editar&id=" + existente.getIdUsuario() + "&erro=validacao");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro na conversão de números no formulário.");
            response.sendRedirect("usuario?acao=novo&erro=validacao");
            return;
        }

        response.sendRedirect("usuario?acao=listar");
    }
}
