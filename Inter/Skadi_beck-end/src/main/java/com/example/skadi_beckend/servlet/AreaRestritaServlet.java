package com.example.skadi_beckend.servlet;

import com.example.skadi_beckend.util.Configuracao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/*
 * Botão "Área restrita" do rodapé (landing e sobre nós).
 * O link da AWS muda com frequência, por isso fica na variável AWS_URL_AREA_RESTRITA (.env ou ambiente)
 * e é lido a cada clique.
 */
@WebServlet("/area-restrita")
public class AreaRestritaServlet extends HttpServlet {

    public static final String CHAVE_LINK = "AWS_URL_AREA_RESTRITA";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String link = Configuracao.obter(CHAVE_LINK);

        // Só aceita links http(s) completos; sem link configurado, abre o login da equipe Skadi
        if (linkValido(link)) {
            response.setHeader("Cache-Control", "no-store");
            response.sendRedirect(link);
        } else {
            System.out.println("Variável " + CHAVE_LINK + " não configurada; abrindo o login da área restrita.");
            response.sendRedirect(request.getContextPath() + "/login-super-admin.jsp");
        }
    }

    // Confere se o valor é um endereço web com protocolo e domínio (ex.: https://exemplo.aws.com)
    private boolean linkValido(String link) {
        if (link == null) return false;
        try {
            URI uri = new URI(link);
            String protocolo = uri.getScheme();
            return ("https".equalsIgnoreCase(protocolo) || "http".equalsIgnoreCase(protocolo))
                    && uri.getHost() != null && !uri.getHost().isBlank();
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
