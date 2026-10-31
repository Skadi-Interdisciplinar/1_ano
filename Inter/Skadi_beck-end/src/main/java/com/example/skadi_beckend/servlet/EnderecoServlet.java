package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.dao.CDDAO;
import com.example.skadi_beckend.dao.EnderecoDAO;
import com.example.skadi_beckend.model.CD;
import com.example.skadi_beckend.model.Endereco;
import com.example.skadi_beckend.model.Usuario;
import com.example.skadi_beckend.util.Acesso;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Servlet responsável por gerenciar as requisições HTTP da entidade Endereco.
 */

@WebServlet("/endereco")
public class EnderecoServlet extends HttpServlet {

    private EnderecoDAO enderecoDao;
    private CDDAO cdDao;

    @Override
    public void init() {
        this.enderecoDao = new EnderecoDAO();
        this.cdDao = new CDDAO();
    }

//    Metodo de leitura e navegacao (listar, carregar formulario, excluir)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Só administradores com CD configurada acessam os endereços.
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
                    excluirEndereco(request, response);
                    break;
                case "buscarPorCidade": // Filtro diversificado (Req 8)
                    buscarPorCidade(request, response);
                    break;
                case "listar":
                default:
                    listarEnderecos(request, response);
                    break;
            }
        } catch (Exception e) {
            System.out.println("Erro no GET (Endereco): " + e.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar a solicitação.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Repete a autorização no envio para não confiar apenas na tela.
        if (!Acesso.exigirAdminOuSuperAdmin(request, response)
                || !Acesso.exigirCdConfigurada(request, response)) return;
        String acao = request.getParameter("acao");

        if ("salvar".equals(acao)) {
            salvarEndereco(request, response);
        } else {
            response.sendRedirect("endereco?acao=listar");
        }
    }

//  Metodos auxiliares

//   Listar
    private void listarEnderecos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario ator = Acesso.usuario(request);
        // Admin recebe endereços da sua CD; super_admin consulta todas.
        List<Endereco> lista = Acesso.ehSuperAdmin(ator)
                ? enderecoDao.listarEnderecosComNomeCD() : enderecoDao.listarPorCd(ator.getIdCd());
        request.setAttribute("listaEnderecos", lista);
        request.getRequestDispatcher("/lista-endereco.jsp").forward(request, response);
    }

//   Buscar por cidade
    private void buscarPorCidade(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String trecho = request.getParameter("buscaCidade");
        Usuario ator = Acesso.usuario(request);
        List<Endereco> lista;

        if (trecho != null && !trecho.trim().isEmpty()) {
            lista = Acesso.ehSuperAdmin(ator)
                    ? enderecoDao.buscarPorCidade(trecho)
                    : enderecoDao.buscarPorCidadeECd(trecho, ator.getIdCd());
        } else {
            lista = Acesso.ehSuperAdmin(ator)
                    ? enderecoDao.listarEnderecosComNomeCD()
                    : enderecoDao.listarPorCd(ator.getIdCd());
        }

        request.setAttribute("listaEnderecos", lista);
        request.getRequestDispatcher("/lista-endereco.jsp").forward(request, response);
    }

    private void abrirFormularioNovo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Usuario ator = Acesso.usuario(request);
        // Admin recebe no formulário somente a CD vinculada à sua conta.
        List<CD> listaCds = Acesso.ehSuperAdmin(ator)
                ? cdDao.listarTodos()
                : java.util.Collections.singletonList(cdDao.buscarPorId(ator.getIdCd()));
        request.setAttribute("listaCds", listaCds);
        request.getRequestDispatcher("/form-endereco.jsp").forward(request, response);
    }

    private void abrirFormularioEdicao(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Usuario ator = Acesso.usuario(request);
            // Impede abrir edição de endereço que pertence a outra CD.
            Endereco endereco = Acesso.ehSuperAdmin(ator)
                    ? enderecoDao.buscarPorId(id)
                    : enderecoDao.buscarPorIdECd(id, ator.getIdCd());
            if (endereco == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Endereço fora da sua CD.");
                return;
            }

            List<CD> listaCds = Acesso.ehSuperAdmin(ator)
                    ? cdDao.listarTodos()
                    : java.util.Collections.singletonList(cdDao.buscarPorId(ator.getIdCd()));
            request.setAttribute("listaCds", listaCds);
            request.setAttribute("endereco", endereco);

            request.getRequestDispatcher("/form-endereco.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendRedirect("endereco?acao=listar");
        }
    }

//   Excluir
    private void excluirEndereco(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Usuario ator = Acesso.usuario(request);
            // Admin só remove endereço da própria CD; super_admin tem alcance global.
            if (Acesso.ehSuperAdmin(ator)) enderecoDao.deletar(id);
            else enderecoDao.deletarPorIdECd(id, ator.getIdCd());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido fornecido para exclusão.");
        }
        response.sendRedirect("endereco?acao=listar");
    }

//   Salvar
    private void salvarEndereco(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");
        String idCdParam = request.getParameter("idCd");
        String rua = request.getParameter("rua");
        String numeroParam = request.getParameter("numero");
        String complemento = request.getParameter("complemento");
        String bairro = request.getParameter("bairro");
        String cidade = request.getParameter("cidade");
        String estado = request.getParameter("estado");
        String cep = request.getParameter("cep");

//        Tratamento e limpeza de dados
        if (rua != null) rua = rua.trim();
        if (bairro != null) bairro = bairro.trim();
        if (cidade != null) cidade = cidade.trim();
        if (estado != null) estado = estado.trim().toUpperCase();
        if (cep != null) cep = cep.replaceAll("\\D", ""); // Limpa o CEP mantendo só os números

//        Validacao
        Usuario ator = Acesso.usuario(request);
        // id_cd do admin vem da sessão, nunca do valor enviado pelo navegador.
        if ((Acesso.ehSuperAdmin(ator) && (idCdParam == null || idCdParam.trim().isEmpty()))
                || rua == null || rua.isEmpty() || bairro == null || bairro.isEmpty() ||
                cidade == null || cidade.isEmpty() || estado == null || estado.length() != 2 ||
                cep == null || cep.length() != 8) {

            System.out.println("Validação falhou: Campos obrigatórios em branco, estado inválido ou CEP incorreto.");
            response.sendRedirect("endereco?acao=novo&erro=validacao");
            return;
        }

        try {
            int idCd;
            if (Acesso.ehSuperAdmin(ator)) {
                idCd = Integer.parseInt(idCdParam);
                if (cdDao.buscarPorId(idCd) == null) {
                    response.sendRedirect("endereco?acao=novo&erro=validacao");
                    return;
                }
            } else {
                idCd = ator.getIdCd();
            }
            Integer numero = (numeroParam != null && !numeroParam.trim().isEmpty()) ? Integer.parseInt(numeroParam) : null;

            if (idParam == null || idParam.isEmpty()) {
                Endereco novo = new Endereco(idCd, rua, numero, complemento, bairro, cidade, estado, cep);
                enderecoDao.inserir(novo);
            } else {
                int id = Integer.parseInt(idParam);
                Endereco atualizado = new Endereco(id, idCd, rua, numero, complemento, bairro, cidade, estado, cep);
                if (Acesso.ehSuperAdmin(ator)) {
                    enderecoDao.atualizar(atualizado);
                } else {
                    if (enderecoDao.buscarPorIdECd(id, idCd) == null) {
                        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Endereço fora da sua CD.");
                        return;
                    }
                    enderecoDao.atualizarPorCd(atualizado, idCd);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro na conversão de números no formulário (ID ou Número da Rua).");
        }

        response.sendRedirect("endereco?acao=listar");
    }
}
