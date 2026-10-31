
package com.example.skadi_beckend.util;

import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EmailUtil {

    /**
     * Envia a mensagem do formulário de contato para CONTACT_EMAIL, ou para
     * EMAIL_USER quando CONTACT_EMAIL não estiver configurada
     */
    public static boolean enviarContato(
            String nomeCompleto, String emailRemetente, String nomeEmpresa, String mensagemContato) {

        String emailTemporario = obterConfiguracao("EMAIL_USER");
        String senhaTemporaria = obterConfiguracao("EMAIL_PASS");

        if (emailTemporario == null || senhaTemporaria == null) {
            System.err.println("ERRO: Credenciais de e-mail (EMAIL_USER / EMAIL_PASS) não foram encontradas!");
            return false;
        }

        String destinatario = obterConfiguracao("CONTACT_EMAIL");
        if (destinatario == null) {
            destinatario = emailTemporario;
        }

        Properties props = criarPropriedadesSmtp();
        Session session = criarSessaoSmtp(props, emailTemporario, senhaTemporaria);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailTemporario));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario, true));
            message.setReplyTo(new Address[]{new InternetAddress(emailRemetente, true)});
            message.setSubject("Nova mensagem de contato pelo site Skadi", "UTF-8");

            String corpo = "Nova mensagem enviada pelo formulário de contato da Skadi.\n\n"
                    + "Nome completo: " + nomeCompleto + "\n"
                    + "E-mail para resposta: " + emailRemetente + "\n"
                    + "Empresa: " + nomeEmpresa + "\n\n"
                    + "Mensagem:\n" + mensagemContato;
            message.setText(corpo, "UTF-8");

            Transport.send(message);
            System.out.println("Mensagem de contato enviada para: " + destinatario);
            return true;
        } catch (MessagingException e) {
            System.err.println("Erro ao enviar mensagem de contato: " + e.getMessage());
            return false;
        }
    }

    private static String obterConfiguracao(String chave) {
        String valor = System.getProperty(chave);
        if (valor == null || valor.isBlank()) {
            valor = System.getenv(chave);
        }
        if (valor == null || valor.isBlank()) {
            try {
                valor = Dotenv.configure().ignoreIfMissing().load().get(chave);
            } catch (Exception e) {
                // O arquivo .env e opcional; variaveis de VM ou do sistema podem ser usadas.
            }
        }
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    private static Properties criarPropriedadesSmtp() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        return props;
    }

    private static Session criarSessaoSmtp(Properties props, String email, String senha) {
        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, senha);
            }
        });
    }

    // Enviar código de recuperação de senha
    public static boolean enviarCodigoRecuperacao(
            String destinatario, String codigo) {

        // Configurações do servidor SMTP do Gmail
        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Busca as credenciais nas propriedades da VM, variaveis do sistema ou .env.
        String emailTemporario = obterConfiguracao("EMAIL_USER");
        String senhaTemporaria = obterConfiguracao("EMAIL_PASS");

        // Trava de segurança: verifica se as credenciais foram encontradas
        if (emailTemporario == null || emailTemporario.isBlank()
                || senhaTemporaria == null || senhaTemporaria.isBlank()) {
            System.err.println("ERRO: Credenciais de e-mail (EMAIL_USER / EMAIL_PASS) " + "não foram encontradas!");
            return false;
        }

        final String meuEmail = emailTemporario;
        final String minhaSenha = senhaTemporaria;

        // Cria a sessão de autenticação do Gmail
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(meuEmail, minhaSenha);
            }
        });

        try {
//          Configura os dados básicos do e-mail
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(meuEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(destinatario)
            );
            message.setSubject("Skadi - Código de Recuperação de Senha", "UTF-8");

            // Template do e-mail em HTML
            String codigoSeguro = escaparHtml(codigo);

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
                    + "              <img src=\"cid:logo\" width=\"150\" alt=\"Skadi\" style=\"display:block;width:150px;max-width:100%;height:auto;\">"
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

            // Cria a parte HTML da mensagem
            MimeBodyPart htmlPart = new MimeBodyPart();

            htmlPart.setContent(htmlContent, "text/html; charset=UTF-8");

            // Carrega a logo da pasta Imagens
            byte[] logoBytes = carregarLogo();

            // Incorpora a logo ao e-mail usando o identificador CID
            MimeBodyPart logoPart = new MimeBodyPart();

            logoPart.setDataHandler( new DataHandler( new ByteArrayDataSource( logoBytes, "image/png")));

            logoPart.setHeader("Content-ID", "<logo>");
            logoPart.setDisposition(MimeBodyPart.INLINE);

            // Junta o HTML e a logo na mesma mensagem
            MimeMultipart related = new MimeMultipart("related");

            related.addBodyPart(htmlPart);
            related.addBodyPart(logoPart);

            MimeBodyPart textoSimples = new MimeBodyPart();
            textoSimples.setText(
                    "Seu código para redefinir a senha é: " + codigo
                            + ". Se você não solicitou este código, ignore esta mensagem.",
                    "UTF-8");

            // Mantém a imagem CID junto do HTML e oferece ambas as versões do e-mail.
            MimeBodyPart htmlComLogo = new MimeBodyPart();
            htmlComLogo.setContent(related);

            MimeMultipart alternative = new MimeMultipart("alternative");
            alternative.addBodyPart(textoSimples);
            alternative.addBodyPart(htmlComLogo);

            message.setContent(alternative);

            // Envia o e-mail
            Transport.send(message);

            System.out.println("E-mail enviado para: " + destinatario);
            return true;

        } catch (MessagingException | IOException e) {
            System.err.println("Erro ao enviar e-mail: " + e.getMessage());
            return false;
        }
    }

    // Carrega a logo dos recursos da aplicação ou da pasta do projeto
    private static byte[] carregarLogo() throws IOException {
        try (InputStream input = EmailUtil.class.getResourceAsStream("/email/logo.png")) {
            if (input == null) {
                throw new IOException("Logo not found at /email/logo.png.");
            }
            return input.readAllBytes();
        }
    }
    //  Escapa caracteres especiais antes de inserir o código no HTML
    private static String escaparHtml(String valor) {
        return valor
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
