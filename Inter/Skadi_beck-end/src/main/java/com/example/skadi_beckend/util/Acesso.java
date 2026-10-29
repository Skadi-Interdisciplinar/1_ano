package com.example.skadi_beckend.util;

import com.example.skadi_beckend.dao.CDDAO;
import com.example.skadi_beckend.model.CD;
import com.example.skadi_beckend.model.Usuario;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;

/**
 * Regras de acesso do Skadi (níveis e unidades), em um só lugar.
 *
 * Níveis (Usuario.nivel_acesso):
 *  - operador    : só vê e resolve avisos (não usa os CRUDs);
 *  - gestor      : só visualiza os dados da própria unidade;
 *  - admin       : cadastra tudo, nas unidades que tem em admin_cd;
 *  - super_admin : equipe Skadi, acessa tudo e é quem adiciona os admins.
 */
public final class Acesso {

    /** Atributo de sessão com o usuário autenticado (gravado pelo LoginServlet). */
    public static final String USUARIO = "usuarioLogado";
    /** Atributos de sessão com a unidade que a pessoa escolheu na tela de unidades. */
    public static final String CD_ATUAL = "cdAtual";
    public static final String CD_ATUAL_NOME = "cdAtualNome";

    private static final String CACHE_CDS = "skadi.cdsPermitidos";
    private static final CDDAO CD_DAO = new CDDAO();

    private Acesso() {
    }

    // ----- usuário e nível -----

    public static Usuario logado(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        return sessao == null ? null : (Usuario) sessao.getAttribute(USUARIO);
    }

    public static boolean ehSuperAdmin(Usuario usuario) {
        return usuario != null && "super_admin".equals(usuario.getNivelAcesso());
    }

    public static boolean ehAdmin(Usuario usuario) {
        return usuario != null && "admin".equals(usuario.getNivelAcesso());
    }

    public static boolean ehGestor(Usuario usuario) {
        return usuario != null && "gestor".equals(usuario.getNivelAcesso());
    }

    /** Cadastrar, editar e excluir: admin e super_admin. */
    public static boolean podeEscrever(Usuario usuario) {
        return ehAdmin(usuario) || ehSuperAdmin(usuario);
    }

    /** Ver os dados administrativos: quem escreve, mais o gestor (só leitura). */
    public static boolean podeLer(Usuario usuario) {
        return podeEscrever(usuario) || ehGestor(usuario);
    }

    // ----- unidades -----

    /** Unidades que a pessoa pode acessar (guardado na requisição para não repetir a consulta). */
    @SuppressWarnings("unchecked")
    public static List<CD> cdsPermitidos(HttpServletRequest request) {
        List<CD> cache = (List<CD>) request.getAttribute(CACHE_CDS);
        if (cache != null) {
            return cache;
        }

        Usuario usuario = logado(request);
        List<CD> lista = new ArrayList<>();

        if (ehSuperAdmin(usuario)) {
            lista = CD_DAO.listarTodos();
        } else if (ehAdmin(usuario)) {
            lista = CD_DAO.listarPorAdmin(usuario.getIdUsuario(), usuario.getIdCd());
        } else if (usuario != null) {
            CD propria = CD_DAO.buscarPorId(usuario.getIdCd());
            if (propria != null) {
                lista.add(propria);
            }
        }

        request.setAttribute(CACHE_CDS, lista);
        return lista;
    }

    public static boolean cdPermitido(HttpServletRequest request, int idCd) {
        for (CD cd : cdsPermitidos(request)) {
            if (cd.getIdCd() == idCd) {
                return true;
            }
        }
        return false;
    }

    /** Unidade escolhida na sessão, ou null se ainda não escolheu (ou se perdeu a permissão). */
    public static Integer cdAtual(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        if (sessao == null) {
            return null;
        }
        Integer idCd = (Integer) sessao.getAttribute(CD_ATUAL);
        return (idCd != null && cdPermitido(request, idCd)) ? idCd : null;
    }

    public static void definirCdAtual(HttpServletRequest request, CD cd) {
        HttpSession sessao = request.getSession();
        sessao.setAttribute(CD_ATUAL, cd.getIdCd());
        sessao.setAttribute(CD_ATUAL_NOME, cd.getNome());
    }

    public static void limparCdAtual(HttpServletRequest request) {
        HttpSession sessao = request.getSession(false);
        if (sessao != null) {
            sessao.removeAttribute(CD_ATUAL);
            sessao.removeAttribute(CD_ATUAL_NOME);
        }
    }

    // ----- filtro por unidade -----

    /** Só os itens da unidade escolhida na sessão (lista vazia se ainda não escolheu). */
    public static <T> List<T> daUnidade(HttpServletRequest request, List<T> todos, ToIntFunction<T> idCdDoItem) {
        Integer idCd = cdAtual(request);
        List<T> filtrados = new ArrayList<>();
        if (idCd == null) {
            return filtrados;
        }
        for (T item : todos) {
            if (idCdDoItem.applyAsInt(item) == idCd) {
                filtrados.add(item);
            }
        }
        return filtrados;
    }
}
