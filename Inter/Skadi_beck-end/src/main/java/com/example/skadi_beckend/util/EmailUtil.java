package com.example.skadi_beckend.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

/*
 * Envio de e-mails da Skadi pela API do Brevo.
 *
 * Antes o envio era por SMTP (smtp.gmail.com, porta 587), mas o plano gratuito do Render
 * bloqueia as portas de e-mail (25, 465 e 587). A API do Brevo é chamada por HTTPS (porta 443),
 * que o Render não bloqueia, então o envio funciona tanto no Render quanto no computador local.
 *
 * Configurações usadas (.env, variáveis do Render ou VM Options):
 * - BREVO_API_KEY: chave de API criada no painel do Brevo (SMTP & API > API Keys)
 * - EMAIL_USER: e-mail remetente; precisa estar confirmado no Brevo (Senders, domains & dedicated IPs)
 * - CONTACT_EMAIL: (opcional) quem recebe o "Contate-nos"; sem ela, vai para o EMAIL_USER
 * - EMAIL_LOGO_URL: (opcional) link público da logo usada no e-mail
 *
 * Observação: como o remetente é um @gmail.com (domínio que não pode ser autenticado no Brevo),
 * o Brevo pode trocar o endereço de envio por um @brevosend.com. O nome "Skadi" continua aparecendo
 * e as respostas vão para o EMAIL_USER, porque todo e-mail sai com o "responder para" preenchido.
 */
public class EmailUtil {

    // Endereço da API do Brevo que envia e-mails transacionais
    private static final String URL_API_BREVO = "https://api.brevo.com/v3/smtp/email";

    // Nome que aparece como remetente na caixa de entrada
    private static final String NOME_REMETENTE = "Skadi";

    // O Brevo não aceita imagem embutida no e-mail (cid:logo), então a logo é carregada por um link público.
    // Por padrão usa a mesma logo do projeto (resources/email/logo.png) direto do GitHub
    private static final String LOGO_PADRAO =
            "https://raw.githubusercontent.com/Skadi-Interdisciplinar/1_ano/main/Inter/Skadi_beck-end/src/main/resources/email/logo.png";

    // Um único cliente HTTP é reaproveitado em todos os envios
    private static final HttpClient CLIENTE_HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    /**
     * Envia a mensagem do formulário de contato para CONTACT_EMAIL, ou para
     * EMAIL_USER quando CONTACT_EMAIL não estiver configurada
     */
    public static boolean enviarContato(
            String nomeCompleto, String emailRemetente, String nomeEmpresa, String mensagemContato) {

        // Busca o remetente nas propriedades da VM, variáveis do sistema ou .env
        String emailSkadi = Configuracao.obter("EMAIL_USER");
        if (emailSkadi == null) {
            System.err.println("ERRO: E-mail remetente (EMAIL_USER) não foi encontrado!");
            return false;
        }

        // Sem CONTACT_EMAIL, a própria conta da Skadi recebe a mensagem
        String destinatario = Configuracao.obter("CONTACT_EMAIL");
        if (destinatario == null) {
            destinatario = emailSkadi;
        }

//      Versão em texto simples (para leitores de e-mail sem HTML)
        String corpoTexto = "Nova mensagem enviada pelo formulário de contato da Skadi.\n\n"
                + "Nome completo: " + nomeCompleto + "\n"
                + "E-mail para resposta: " + emailRemetente + "\n"
                + "Empresa: " + nomeEmpresa + "\n\n"
                + "Mensagem:\n" + mensagemContato;

//      Versão em HTML: o Brevo exige o corpo em HTML.
//      Os dados vêm do formulário do site, por isso são escapados antes de entrar no HTML
        String corpoHtml = "<!doctype html>"
                + "<html lang=\"pt-BR\">"
                + "<body style=\"margin:0;padding:24px;font-family:Manrope,Arial,sans-serif;color:#0f2e4f;\">"
                + "  <p>Nova mensagem enviada pelo formulário de contato da Skadi.</p>"
                + "  <p><strong>Nome completo:</strong> " + escaparHtml(nomeCompleto) + "<br>"
                + "  <strong>E-mail para resposta:</strong> " + escaparHtml(emailRemetente) + "<br>"
                + "  <strong>Empresa:</strong> " + escaparHtml(nomeEmpresa) + "</p>"
                + "  <p><strong>Mensagem:</strong></p>"
                // white-space:pre-wrap mantém as quebras de linha que a pessoa digitou
                + "  <p style=\"white-space:pre-wrap;\">" + escaparHtml(mensagemContato) + "</p>"
                + "</body>"
                + "</html>";

        // Monta o e-mail; aqui o "responder" vai para quem preencheu o formulário,
        // assim a equipe responde o cliente direto pela caixa de entrada
        JsonObject email = montarEmail(emailSkadi, destinatario, emailRemetente,
                "Nova mensagem de contato pelo site Skadi", corpoHtml, corpoTexto);

        if (enviarPeloBrevo(email)) {
            System.out.println("Mensagem de contato enviada para: " + destinatario);
            return true;
        }
        return false;
    }

    // Enviar código de recuperação de senha
    public static boolean enviarCodigoRecuperacao(
            String destinatario, String codigo) {

        // Busca o remetente nas propriedades da VM, variáveis do sistema ou .env
        String emailSkadi = Configuracao.obter("EMAIL_USER");

        // Trava de segurança: verifica se o remetente foi encontrado
        if (emailSkadi == null) {
            System.err.println("ERRO: E-mail remetente (EMAIL_USER) não foi encontrado!");
            return false;
        }

        // Link da logo: usa EMAIL_LOGO_URL se existir, senão a logo do GitHub
        String linkLogo = Configuracao.obter("EMAIL_LOGO_URL");
        if (linkLogo == null) {
            linkLogo = LOGO_PADRAO;
        }

        // Template do e-mail em HTML
        String codigoSeguro = escaparHtml(codigo);
        String logoSegura = escaparHtml(linkLogo);

        String htmlContent = "<!doctype html>"
                + "<html lang=\"pt-BR\">"
                + "<head>"
                + "  <meta charset=\"UTF-8\">"
                + "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                + "  <link href=\"https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700&display=swap\" rel=\"stylesheet\">"
                + "</head>"
                + "<body style=\"margin:0;padding:32px 12px;background-color:#f7fafc;font-family:Manrope,sans-serif;color:#0f2e4f;\">"

                + "  <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"border-collapse:collapse;\">"
                + "    <tr>"
                + "      <td align=\"center\">"

                + "        <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"max-width:520px;border-collapse:separate;border-spacing:0;background:#ffffff;border-radius:18px;overflow:hidden;\">"

                // Cabeçalho com a logo da Skadi
                + "          <tr>"
                + "            <td align=\"center\" style=\"padding:30px 24px 20px;background:#ffffff;\">"
                + "              <img src=\"" + logoSegura + "\" width=\"150\" alt=\"Skadi\" style=\"display:block;width:150px;max-width:100%;height:auto;\">"
                + "            </td>"
                + "          </tr>"

                // Conteúdo principal do e-mail
                + "          <tr>"
                + "            <td style=\"padding:30px 32px 34px;text-align:center;\">"

                + "              <h1 style=\"margin:0 0 12px;color:#0b56a2;font-family:Manrope,sans-serif;font-size:23px;font-weight:600;\">"
                + "                Código de verificação"
                + "              </h1>"

                + "              <p style=\"margin:0 0 22px;color:#526078;font-family:Manrope,sans-serif;font-size:15px;line-height:1.6;\">"
                + "                Use este código para redefinir sua senha."
                + "              </p>"

                // Destaque do código de recuperação
                + "              <div style=\"display:inline-block;padding:15px 26px;border:1px solid #d5e0f3;border-radius:12px;background:#f4f7fc;color:#0b56a2;font-family:Manrope,sans-serif;font-size:32px;font-weight:700;letter-spacing:8px;\">"
                +                    codigoSeguro
                + "              </div>"

                // Aviso de segurança e validade do código
                + "              <p style=\"margin:22px 0 0;color:#526078;font-family:Manrope,sans-serif;font-size:13px;line-height:1.7;\">"
                + "                O código expira em 10 minutos. Se você não solicitou este código, ignore esta mensagem."
                + "              </p>"

                + "            </td>"
                + "          </tr>"

                // Rodapé com a descrição da Skadi
                + "          <tr>"
                + "            <td style=\"padding:14px 20px;background:#f4f7fc;text-align:center;color:#718096;font-family:Manrope,sans-serif;font-size:11px;\">"
                + "              Skadi · A tecnologia de monitoramento que previne o desperdício em seus frigoríficos"
                + "            </td>"
                + "          </tr>"

                + "        </table>"
                + "      </td>"
                + "    </tr>"
                + "  </table>"

                + "</body>"
                + "</html>";

        // Versão em texto simples, usada quando o leitor de e-mail não mostra HTML
        String corpoTexto = "Seu código para redefinir a senha é: " + codigo
                + ". Se você não solicitou este código, ignore esta mensagem.";

//      Monta e envia o e-mail; se o usuário responder, a resposta vai para a própria Skadi
        JsonObject email = montarEmail(emailSkadi, destinatario, emailSkadi,
                "Skadi - Código de Recuperação de Senha", htmlContent, corpoTexto);

        if (enviarPeloBrevo(email)) {
            System.out.println("E-mail enviado para: " + destinatario);
            return true;
        }
        return false;
    }

    // Monta o JSON no formato que a API do Brevo espera
    private static JsonObject montarEmail(String remetente, String destinatario, String responderPara,
                                          String assunto, String corpoHtml, String corpoTexto) {

        // Quem envia: o e-mail da Skadi confirmado no Brevo
        JsonObject quemEnvia = new JsonObject();
        quemEnvia.addProperty("name", NOME_REMETENTE);
        quemEnvia.addProperty("email", remetente);

        // Quem recebe: o Brevo aceita uma lista, aqui sempre com um destinatário
        JsonObject quemRecebe = new JsonObject();
        quemRecebe.addProperty("email", destinatario);

        JsonArray listaDestinatarios = new JsonArray();
        listaDestinatarios.add(quemRecebe);

        // Para quem vai a resposta quando alguém clica em "Responder".
        // Só o e-mail é enviado: o nome digitado no site pode ter caracteres que o Brevo recusa
        JsonObject respostaPara = new JsonObject();
        respostaPara.addProperty("email", responderPara);

        // Junta tudo no corpo da requisição
        JsonObject email = new JsonObject();
        email.add("sender", quemEnvia);
        email.add("to", listaDestinatarios);
        email.add("replyTo", respostaPara);
        email.addProperty("subject", assunto);
        email.addProperty("htmlContent", corpoHtml);
        email.addProperty("textContent", corpoTexto);

        return email;
    }

    // Faz a chamada HTTPS para o Brevo e diz se o e-mail foi aceito
    private static boolean enviarPeloBrevo(JsonObject email) {

        // Busca a chave de API nas propriedades da VM, variáveis do sistema ou .env
        String chaveApi = Configuracao.obter("BREVO_API_KEY");

        // Trava de segurança: sem a chave não há como enviar
        if (chaveApi == null) {
            System.err.println("ERRO: Chave do Brevo (BREVO_API_KEY) não foi encontrada!");
            return false;
        }

        // Requisição POST com o JSON do e-mail; a chave vai no cabeçalho "api-key"
        HttpRequest requisicao = HttpRequest.newBuilder()
                .uri(URI.create(URL_API_BREVO))
                .timeout(Duration.ofSeconds(15))
                .header("api-key", chaveApi)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(email.toString(), StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> resposta = CLIENTE_HTTP.send(
                    requisicao, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

//          O Brevo responde 201 quando aceita o e-mail para envio.
//          Qualquer código 2xx significa sucesso, então aceita a faixa toda (200 a 299)
            if (resposta.statusCode() >= 200 && resposta.statusCode() < 300) {
                return true;
            }

//          Qualquer outro código é erro: a resposta explica o motivo
//          (ex.: 401 = chave inválida, 400 = remetente não confirmado ou dado inválido)
            System.err.println("Erro ao enviar e-mail pelo Brevo (" + resposta.statusCode() + "): " + resposta.body());
            return false;

        } catch (IOException e) {
            // Sem internet, Brevo fora do ar ou tempo esgotado
            // toString() mostra o tipo do erro mesmo quando ele não traz mensagem (ex.: ConnectException)
            System.err.println("Erro de conexão com o Brevo: " + e);
            return false;
        } catch (InterruptedException e) {
            // Mantém o aviso de interrupção para o servidor
            Thread.currentThread().interrupt();
            System.err.println("Envio de e-mail interrompido: " + e.getMessage());
            return false;
        }
    }

    //  Escapa caracteres especiais antes de inserir textos no HTML
    private static String escaparHtml(String valor) {
        if (valor == null) {
            return "";
        }
        return valor
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
