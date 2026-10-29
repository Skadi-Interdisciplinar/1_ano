package com.example.skadi_beckend.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Redirecionamentos de erro usados pelos servlets dos CRUDs.
 */
public final class Resposta {

    private Resposta() {
    }

    /**
     * Volta para o formulário com a mensagem de erro: "novo" se o registro ainda não existe,
     * "editar" (com o id) se a pessoa estava editando, para ela não perder o contexto.
     */
    public static void erroFormulario(HttpServletResponse response, String rota, String idParam, String erro) throws IOException {
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendRedirect(rota + "?acao=novo&erro=" + erro);
        } else {
            response.sendRedirect(rota + "?acao=editar&id=" + idParam.trim() + "&erro=" + erro);
        }
    }

    /** Volta para a lista, com ou sem aviso de erro. */
    public static void lista(HttpServletResponse response, String rota, String erro) throws IOException {
        response.sendRedirect(rota + "?acao=listar" + (erro == null ? "" : "&erro=" + erro));
    }
}
