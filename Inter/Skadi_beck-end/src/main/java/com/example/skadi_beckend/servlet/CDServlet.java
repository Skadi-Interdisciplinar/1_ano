package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.CDDAO;
import com.example.skadi_beckend.dao.EnderecoDAO;
import com.example.skadi_beckend.dao.FrigorificoDAO;
import com.example.skadi_beckend.dao.UsuarioDAO;
import com.example.skadi_beckend.model.CD;
import com.example.skadi_beckend.model.Endereco;
import com.example.skadi_beckend.model.Usuario;
import com.example.skadi_beckend.util.Acesso;
import com.example.skadi_beckend.util.Validator; // <-- IMPORTAÇÃO DA SUA CLASSE

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet responsável por gerenciar as requisições HTTP da entidade CD.
 *
 * Admin: pode ter várias CDs (tabela admin_cd). Ele cadastra, edita e exclui as suas CDs,
 * e a CD que escolhe na tela de unidades vira a "unidade ativa" da sessão (usuarioLogado.idCd),
 * que é a CD usada pelas outras telas (funcionários, frigoríficos, termômetros, lotes...).
 * Super_admin: vê e gerencia todas as CDs.
 */
@WebServlet("/cd")
public class CDServlet extends HttpServlet {

    private CDDAO dao;
    private UsuarioDAO usuarioDao;
    private EnderecoDAO enderecoDao;
    private FrigorificoDAO frigorificoDao;

    @Override
    public void init() {
        this.dao = new CDDAO();
        this.usuarioDao = new UsuarioDAO();
        this.enderecoDao = new EnderecoDAO();
        this.frigorificoDao = new FrigorificoDAO();
    }

//    Metodo de leitura e navegacao (listar, carregar formulario, excluir)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Admin e super_admin podem acessar as CDs; cada ação ainda valida o escopo permitido.
        if (!Acesso.exigirAdminOuSuperAdmin(request, response)) return;
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
                    excluirCD(request, response);
                    break;
                case "excluirPorCnpj":
                    excluirCDPorCnpj(request, response);
                    break;
                case "selecionar":
                    selecionarCD(request, response);
                    break;
                case "buscarPorNome":
                    buscarPorNome(request, response);
                    break;
                case "listar":
                default:
                    listarCDs(request, response);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Erro no processamento da requisição GET (CD): " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ocorreu um erro ao processar sua solicitação.");
        }
    }

//    Metodo enviar dados do formulario (salvar / atualizar)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Confere o papel também no envio de formulários.
        if (!Acesso.exigirAdminOuSuperAdmin(request, response)) return;
        String acao = request.getParameter("acao");

        if ("salvar".equals(acao)) {
            salvarCD(request, response);
        } else {
            response.sendRedirect("cd?acao=listar");
        }
    }

//    Metodos auxiliares

//   Listar
    private void listarCDs(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario ator = Acesso.usuario(request);

        // super_admin vê todas as CDs
        if (Acesso.ehSuperAdmin(ator)) {
            request.setAttribute("listaCds", dao.listarTodos());
            request.setAttribute("ehSuperAdmin", true);
            request.getRequestDispatcher("/lista-cd.jsp").forward(request, response);
            return;
        }

        // admin vê todas as CDs dele e é direcionado ao cadastro se ainda não tiver nenhuma
        List<CD> listaCds = dao.listarPorAdmin(ator.getIdUsuario());
        if (listaCds.isEmpty()) {
            ator.setIdCd(null);
            response.sendRedirect("cd?acao=novo");
            return;
        }

        // Garante que a unidade ativa da sessão é uma CD dele (ex.: depois de excluir a CD que estava ativa)
        if (!contemCd(listaCds, ator.getIdCd())) {
            ator.setIdCd(listaCds.get(0).getIdCd());
        }

        request.setAttribute("listaCds", listaCds);
        request.setAttribute("resumoCds", montarResumo(listaCds));
        request.setAttribute("ehSuperAdmin", false);
        request.getRequestDispatcher("/lista-cd.jsp").forward(request, response);
    }

//   Resumo de cada CD do admin para os cartões da tela de unidades (endereço e contagens).
//   As outras telas só trazem dados da unidade ativa, por isso o resumo das CDs do admin vem daqui.
//   (O super_admin continua montando os cartões com as listas completas, no cds.js.)
    private Map<Integer, Map<String, Object>> montarResumo(List<CD> listaCds) {
        Map<Integer, Map<String, Object>> resumo = new HashMap<>();

        for (CD cd : listaCds) {
            int idCd = cd.getIdCd();
            Map<String, Object> dados = new HashMap<>();

            // um endereço por unidade
            List<Endereco> enderecos = enderecoDao.listarPorCd(idCd);
            dados.put("endereco", enderecos.isEmpty() ? null : enderecos.get(0));

            // mesmas contagens que o cds.js faz para o super_admin
            dados.put("frigorificosTotal", frigorificoDao.listarPorCd(idCd).size());
            dados.put("frigorificosAtivos", frigorificoDao.listarComTermometrosAtivosPorCd(idCd).size());
            dados.put("funcionariosTotal", usuarioDao.listarFuncionariosPorCd(idCd, null).size());

            resumo.put(idCd, dados);
        }
        return resumo;
    }

//   Confere se o id está na lista de CDs
    private boolean contemCd(List<CD> listaCds, Integer idCd) {
        if (idCd == null) return false;
        for (CD cd : listaCds) {
            if (cd.getIdCd() == idCd) return true;
        }
        return false;
    }

//   Admin e super_admin podem mexer nesta CD?
//   super_admin: qualquer CD | admin: só as CDs dele (admin_cd)
    private boolean podeGerenciarCd(Usuario ator, int idCd) {
        return Acesso.ehSuperAdmin(ator)
                || (Acesso.ehAdmin(ator) && dao.pertenceAoAdmin(ator.getIdUsuario(), idCd));
    }

//   Selecionar unidade (cartão da tela de unidades)
//   Admin: a CD escolhida vira a unidade ativa da sessão, usada pelas outras telas.
//   Super_admin: a escolha fica só no navegador (as telas filtram pela unidade), então só segue em frente.
    private void selecionarCD(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Usuario ator = Acesso.usuario(request);
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            if (!podeGerenciarCd(ator, id)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "CD fora da sua permissão.");
                return;
            }
            if (Acesso.ehAdmin(ator)) {
                ator.setIdCd(id);
            }
            response.sendRedirect("usuario?acao=listar");
        } catch (NumberFormatException e) {
            response.sendRedirect("cd?acao=listar");
        }
    }

//   Buscar por nome
    private void buscarPorNome(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String trechoNome = request.getParameter("buscaNome");
        Usuario ator = Acesso.usuario(request);
        List<CD> listaCds;

        // A busca do admin não amplia seu acesso para outras CDs.
        if (Acesso.ehAdmin(ator)) {
            listarCDs(request, response);
            return;
        } else if (trechoNome != null && !trechoNome.trim().isEmpty()) {
            listaCds = dao.buscarPorNome(trechoNome);
        } else {
            listaCds = dao.listarTodos();
        }

        request.setAttribute("listaCds", listaCds);
        request.setAttribute("ehSuperAdmin", true);
        request.getRequestDispatcher("/lista-cd.jsp").forward(request, response);
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario ator = Acesso.usuario(request);
        // Admin e super_admin podem cadastrar várias CDs.
        // Admin sem unidade ativa, mas que já tem CDs (admin_cd), vai para a lista em vez do 1º acesso.
        if (Acesso.ehAdmin(ator) && ator.getIdCd() == null
                && !dao.listarPorAdmin(ator.getIdUsuario()).isEmpty()) {
            response.sendRedirect("cd?acao=listar");
            return;
        }
        request.setAttribute("ehSuperAdmin", Acesso.ehSuperAdmin(ator));
        request.getRequestDispatcher("/form-cd.jsp").forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            CD cdExistente = dao.buscarPorId(id);
            Usuario ator = Acesso.usuario(request);
            // Impede que admin consulte a tela de edição de uma CD de outra conta.
            if (cdExistente == null || !podeGerenciarCd(ator, cdExistente.getIdCd())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "CD fora da sua permissão.");
                return;
            }

            request.setAttribute("cd", cdExistente);
            request.setAttribute("ehSuperAdmin", Acesso.ehSuperAdmin(ator));
            request.getRequestDispatcher("/form-cd.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            System.out.println("Erro de formatação ao buscar ID para edição: " + e.getMessage());
            response.sendRedirect("cd?acao=listar");
        }
    }

//   Excluir CD
    private void excluirCD(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // super_admin exclui qualquer CD; admin exclui só as CDs dele.
        Usuario ator = Acesso.usuario(request);
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            if (!podeGerenciarCd(ator, id)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "CD fora da sua permissão.");
                return;
            }
            dao.deletar(id);
            atualizarUnidadeAtiva(ator, id);
        } catch (NumberFormatException e) {
            System.out.println("ID inválido fornecido para exclusão.");
        }
        response.sendRedirect("cd?acao=listar");
    }

//   Excluir por CNPJ
    private void excluirCDPorCnpj(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Mesma regra da exclusão por ID: admin só exclui as CDs dele.
        Usuario ator = Acesso.usuario(request);
        String cnpj = request.getParameter("cnpj");


        if (Validator.validarFormatoCnpj(cnpj)) {
            // Limpa a pontuação antes de mandar para o banco
            cnpj = cnpj.replaceAll("\\D", "");

            if (Acesso.ehSuperAdmin(ator)) {
                dao.deletarPorCnpj(cnpj);
            } else {
                // procura o CNPJ só entre as CDs do admin
                for (CD cd : dao.listarPorAdmin(ator.getIdUsuario())) {
                    if (cnpj.equals(cd.getCnpj())) {
                        dao.deletar(cd.getIdCd());
                        atualizarUnidadeAtiva(ator, cd.getIdCd());
                        break;
                    }
                }
            }
        }
        response.sendRedirect("cd?acao=listar");
    }

//   Se o admin excluiu a unidade que estava ativa, passa para outra CD dele (ou fica sem, se era a última)
    private void atualizarUnidadeAtiva(Usuario ator, int idExcluida) {
        if (!Acesso.ehAdmin(ator) || ator.getIdCd() == null || ator.getIdCd() != idExcluida) return;
        List<CD> restantes = dao.listarPorAdmin(ator.getIdUsuario());
        ator.setIdCd(restantes.isEmpty() ? null : restantes.get(0).getIdCd());
    }

//   Salvar
    private void salvarCD(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        String nome = request.getParameter("nome");
        String cnpj = request.getParameter("cnpj");


        if (nome == null || nome.trim().isEmpty() || !Validator.validarFormatoCnpj(cnpj)) {
            System.out.println("Validação falhou: Nome vazio ou CNPJ inválido.");
            response.sendRedirect("cd?acao=novo&erro=validacao");
            return;
        }

        // LIMPA A PONTUAÇÃO DO CNPJ ANTES DE SALVAR NO BANCO
        cnpj = cnpj.replaceAll("\\D", "");

        Usuario ator = Acesso.usuario(request);
        if (Acesso.ehAdmin(ator)) {
            // Cada nova CD é criada junto com seu vínculo ao admin (admin_cd) em uma única transação.
            if (idParam == null || idParam.trim().isEmpty()) {
                Integer idCd = usuarioDao.criarCdParaAdministrador(ator.getIdUsuario(), nome.trim(), cnpj);
                if (idCd == null) {
                    response.sendRedirect("cd?acao=novo&erro=validacao");
                    return;
                }
                // primeira CD do admin: já vira a unidade ativa
                if (ator.getIdCd() == null) {
                    ator.setIdCd(idCd);
                }
            } else {
                try {
                    int id = Integer.parseInt(idParam);
                    // admin edita qualquer CD dele, não só a unidade ativa
                    if (!podeGerenciarCd(ator, id)) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "CD fora da sua permissão.");
                        return;
                    }
                    dao.atualizar(new CD(id, nome, cnpj));
                } catch (NumberFormatException e) {
                    response.sendRedirect("cd?acao=listar");
                    return;
                }
            }
        } else if (idParam == null || idParam.isEmpty()) {
            CD novoCD = new CD(nome, cnpj);
            dao.inserir(novoCD);
        } else {
            try {
                int id = Integer.parseInt(idParam);
                CD cdAtualizado = new CD(id, nome, cnpj);
                dao.atualizar(cdAtualizado);
            } catch (NumberFormatException e) {
                System.out.println("Erro ao converter ID no momento de salvar.");
            }
        }
        response.sendRedirect("cd?acao=listar");
    }
}
