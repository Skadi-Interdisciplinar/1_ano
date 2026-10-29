package com.example.skadi_beckend.filter;

import com.example.skadi_beckend.dao.EnderecoDAO;
import com.example.skadi_beckend.dao.FrigorificoDAO;
import com.example.skadi_beckend.dao.TermometroDAO;
import com.example.skadi_beckend.dao.UsuarioDAO;
import com.example.skadi_beckend.model.Endereco;
import com.example.skadi_beckend.model.Frigorifico;
import com.example.skadi_beckend.model.Termometro;
import com.example.skadi_beckend.model.Usuario;
import com.example.skadi_beckend.util.Acesso;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Porteiro do sistema: exige login, confere o nível de acesso e a unidade de cada pedido.
 *
 * Só olha pedidos vindos do navegador (os "forward" dos servlets para as JSPs não passam por aqui).
 */
@WebFilter("/*")
public class AutenticacaoFilter implements Filter {

    /** Páginas abertas a qualquer visitante. */
    private static final Set<String> PUBLICAS = Set.of(
            "", "/", "/landing.jsp", "/sobre-nos.jsp", "/ajuda-trimmy.jsp", "/contato.jsp", "/contato",
            "/login", "/login.jsp", "/login-cliente.jsp", "/login-super-admin.jsp",
            "/esqueci-senha.jsp", "/esqueci-codigo.jsp", "/redefinir-senha.jsp", "/recuperar-senha",
            "/msg-enviada.jsp", "/acesso-negado.jsp");

    private static final List<String> PREFIXOS_PUBLICOS = List.of("/css/", "/js/", "/Imagens/");

    /** Rotas dos CRUDs (os servlets). */
    private static final Set<String> CRUDS = Set.of(
            "/cd", "/usuario", "/frigorifico", "/termometro", "/categoria", "/lote", "/endereco");

    /** CRUDs cujos dados pertencem a uma unidade. */
    private static final Set<String> UNIDADE = Set.of("/usuario", "/frigorifico", "/termometro", "/endereco");

    private final UsuarioDAO usuarioDao = new UsuarioDAO();
    private final FrigorificoDAO frigorificoDao = new FrigorificoDAO();
    private final TermometroDAO termometroDao = new TermometroDAO();
    private final EnderecoDAO enderecoDao = new EnderecoDAO();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String caminho = request.getServletPath();

        if (ehPublica(caminho)) {
            chain.doFilter(req, res);
            return;
        }

        // Daqui para baixo é tudo área logada: o navegador não guarda cópia (voltar depois de sair não mostra dados)
        response.setHeader("Cache-Control", "no-store");

        Usuario usuario = Acesso.logado(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login-cliente.jsp?erro=sessao");
            return;
        }

        // Abrir direto uma página interna (lista-x.jsp / form-x.jsp) não traz dados: manda para o servlet
        String atalho = atalhoParaServlet(caminho);
        if (atalho != null) {
            response.sendRedirect(request.getContextPath() + atalho);
            return;
        }

        if ("/cadastro.jsp".equals(caminho)) {
            if (!Acesso.ehSuperAdmin(usuario)) {
                negar(request, response, "A tela de cadastro é exclusiva da equipe Skadi.");
                return;
            }
            // O cadastro de pessoas da equipe Skadi é o formulário de funcionários (grava de verdade no banco)
            response.sendRedirect(request.getContextPath() + "/usuario?acao=novo");
            return;
        }

        if (CRUDS.contains(caminho) && !permitido(request, response, caminho, usuario)) {
            return;
        }

        chain.doFilter(req, res);
    }

    // ----- regras -----

    private boolean ehPublica(String caminho) {
        if (PUBLICAS.contains(caminho)) {
            return true;
        }
        for (String prefixo : PREFIXOS_PUBLICOS) {
            if (caminho.startsWith(prefixo)) {
                return true;
            }
        }
        return false;
    }

    private String atalhoParaServlet(String caminho) {
        if (caminho.startsWith("/lista-") && caminho.endsWith(".jsp")) {
            return "/" + caminho.substring("/lista-".length(), caminho.length() - ".jsp".length()) + "?acao=listar";
        }
        if (caminho.startsWith("/form-") && caminho.endsWith(".jsp")) {
            return "/" + caminho.substring("/form-".length(), caminho.length() - ".jsp".length()) + "?acao=novo";
        }
        return null;
    }

    /** Devolve true se o pedido pode seguir; senão já respondeu ao navegador. */
    private boolean permitido(HttpServletRequest request, HttpServletResponse response, String caminho, Usuario usuario)
            throws IOException, ServletException {

        String acao = request.getParameter("acao");
        if (acao == null || acao.isEmpty()) {
            acao = "listar";
        }
        boolean post = "POST".equalsIgnoreCase(request.getMethod());
        boolean exclusao = acao.startsWith("excluir");
        boolean escrita = exclusao || "novo".equals(acao) || "editar".equals(acao) || "salvar".equals(acao);

        // Salvar e excluir só por POST (um link ou uma imagem de outro site não pode apagar nada)
        if ((exclusao || "salvar".equals(acao)) && !post) {
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Use o formulário para salvar ou excluir.");
            return false;
        }

        if (escrita && !Acesso.podeEscrever(usuario)) {
            negar(request, response, "Seu nível de acesso só permite visualizar estas informações.");
            return false;
        }
        if (!escrita && !Acesso.podeLer(usuario)) {
            negar(request, response, "Esta área ainda não está disponível para o seu nível de acesso.");
            return false;
        }

        // As listas de uma unidade só abrem depois de escolher a unidade na tela de unidades
        if (!escrita && !"selecionar".equals(acao) && UNIDADE.contains(caminho) && Acesso.cdAtual(request) == null) {
            response.sendRedirect(request.getContextPath() + "/cd?acao=listar");
            return false;
        }

        if ("selecionar".equals(acao)) {
            return conferirUnidade(request, response, caminho, parseId(request.getParameter("id")));
        }
        if (!escrita) {
            return true;
        }
        return conferirRegistro(request, response, caminho, acao, usuario);
    }

    private boolean conferirRegistro(HttpServletRequest request, HttpServletResponse response, String caminho,
                                     String acao, Usuario usuario) throws IOException, ServletException {

        Integer id = parseId(request.getParameter("id"));
        boolean salvando = "salvar".equals(acao);

        switch (caminho) {
            case "/cd":
                if (id != null && !Acesso.cdPermitido(request, id)) {
                    return recusarUnidade(request, response);
                }
                if ("excluir".equals(acao) && id != null && id == usuario.getIdCd()) {
                    negar(request, response, "Não é possível excluir a unidade onde o seu usuário está lotado.");
                    return false;
                }
                return true;

            case "/usuario":
                if (id != null) {
                    Usuario alvo = usuarioDao.buscarPorId(id);
                    if (alvo == null) {
                        return naoEncontrado(response);
                    }
                    if (!Acesso.cdPermitido(request, alvo.getIdCd())) {
                        return recusarUnidade(request, response);
                    }
                    if (!Acesso.ehSuperAdmin(usuario) && ehNivelAdministrativo(alvo.getNivelAcesso())) {
                        negar(request, response, "Somente a equipe Skadi altera administradores.");
                        return false;
                    }
                    if ("excluir".equals(acao) && id == usuario.getIdUsuario()) {
                        negar(request, response, "Você não pode excluir o seu próprio usuário.");
                        return false;
                    }
                }
                if (salvando) {
                    Integer idCd = parseId(request.getParameter("idCd"));
                    if (idCd != null && !Acesso.cdPermitido(request, idCd)) {
                        return recusarUnidade(request, response);
                    }
                    if (!Acesso.ehSuperAdmin(usuario) && ehNivelAdministrativo(request.getParameter("nivelAcesso"))) {
                        negar(request, response, "Somente a equipe Skadi cadastra administradores.");
                        return false;
                    }
                }
                return true;

            case "/frigorifico":
                if (id != null) {
                    Frigorifico alvo = frigorificoDao.buscarPorId(id);
                    if (alvo == null) {
                        return naoEncontrado(response);
                    }
                    if (!Acesso.cdPermitido(request, alvo.getIdCd())) {
                        return recusarUnidade(request, response);
                    }
                }
                return conferirIdCdDoFormulario(request, response, salvando);

            case "/termometro":
                if (id != null) {
                    Termometro alvo = termometroDao.buscarPorId(id);
                    if (alvo == null) {
                        return naoEncontrado(response);
                    }
                    if (!frigorificoPermitido(request, alvo.getIdFrigorifico())) {
                        return recusarUnidade(request, response);
                    }
                }
                if (salvando) {
                    Integer idFrigorifico = parseId(request.getParameter("idFrigorifico"));
                    if (idFrigorifico != null && !frigorificoPermitido(request, idFrigorifico)) {
                        return recusarUnidade(request, response);
                    }
                }
                return true;

            case "/endereco":
                if (id != null) {
                    Endereco alvo = enderecoDao.buscarPorId(id);
                    if (alvo == null) {
                        return naoEncontrado(response);
                    }
                    if (!Acesso.cdPermitido(request, alvo.getIdCd())) {
                        return recusarUnidade(request, response);
                    }
                }
                return conferirIdCdDoFormulario(request, response, salvando);

            default:
                // categoria e lote não pertencem a uma unidade
                return true;
        }
    }

    private boolean conferirUnidade(HttpServletRequest request, HttpServletResponse response, String caminho, Integer id)
            throws IOException, ServletException {
        if ("/cd".equals(caminho) && id != null && !Acesso.cdPermitido(request, id)) {
            return recusarUnidade(request, response);
        }
        return true;
    }

    private boolean conferirIdCdDoFormulario(HttpServletRequest request, HttpServletResponse response, boolean salvando)
            throws IOException, ServletException {
        if (salvando) {
            Integer idCd = parseId(request.getParameter("idCd"));
            if (idCd != null && !Acesso.cdPermitido(request, idCd)) {
                return recusarUnidade(request, response);
            }
        }
        return true;
    }

    private boolean frigorificoPermitido(HttpServletRequest request, int idFrigorifico) {
        Frigorifico frigorifico = frigorificoDao.buscarPorId(idFrigorifico);
        return frigorifico != null && Acesso.cdPermitido(request, frigorifico.getIdCd());
    }

    private boolean ehNivelAdministrativo(String nivel) {
        return "admin".equals(nivel) || "super_admin".equals(nivel);
    }

    private Integer parseId(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.valueOf(texto.trim());
        } catch (NumberFormatException erro) {
            return null;
        }
    }

    // ----- respostas de recusa -----

    private boolean recusarUnidade(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        negar(request, response, "Você não tem acesso a esta unidade.");
        return false;
    }

    private boolean naoEncontrado(HttpServletResponse response) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Registro não encontrado.");
        return false;
    }

    private void negar(HttpServletRequest request, HttpServletResponse response, String motivo) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        request.setAttribute("motivoNegado", motivo);
        request.getRequestDispatcher("/acesso-negado.jsp").forward(request, response);
    }
}
