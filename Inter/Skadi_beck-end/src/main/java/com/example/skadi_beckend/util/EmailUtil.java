package com.example.skadi_beckend.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

/**
 * Envio de e-mails do Skadi (código de recuperação de senha e mensagens da página de contato).
 * As credenciais vêm das variáveis EMAIL_USER / EMAIL_PASS (no Render, em Environment).
 */
public class EmailUtil {

    /** Tempo máximo (ms) de conexão/envio: sem isso um SMTP fora do ar deixa a requisição pendurada. */
    private static final String TIMEOUT = "10000";

    // Variável da JVM (-D) tem prioridade; depois a variável de ambiente
    private static String config(String nome) {
        String valor = System.getProperty(nome);
        return (valor != null && !valor.isEmpty()) ? valor : System.getenv(nome);
    }

    private static Session criarSessao(final String usuario, final String senha) {
        // SMTP_HOST / SMTP_PORT são opcionais (padrão: Gmail); servem para testar sem enviar e-mail de verdade
        String host = config("SMTP_HOST");
        String porta = config("SMTP_PORT");
        boolean gmail = (host == null || host.isEmpty());

        Properties props = new Properties();
        props.put("mail.smtp.host", gmail ? "smtp.gmail.com" : host);
        props.put("mail.smtp.port", (porta == null || porta.isEmpty()) ? "587" : porta);
        props.put("mail.smtp.auth", gmail ? "true" : "false");
        props.put("mail.smtp.starttls.enable", gmail ? "true" : "false");
        props.put("mail.smtp.connectiontimeout", TIMEOUT);
        props.put("mail.smtp.timeout", TIMEOUT);
        props.put("mail.smtp.writetimeout", TIMEOUT);

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, senha);
            }
        });
    }

    private static String[] credenciais() throws MessagingException {
        String email = config("EMAIL_USER");
        String senha = config("EMAIL_PASS");
        if (email == null || senha == null) {
            throw new MessagingException("Credenciais de e-mail (EMAIL_USER / EMAIL_PASS) não foram encontradas!");
        }
        return new String[]{email, senha};
    }

//  Enviar codigo
    public static void enviarCodigoRecuperacao(String destinatario, String codigo) throws MessagingException {
        String[] cred = credenciais();
        Session session = criarSessao(cred[0], cred[1]);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(cred[0]));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject("Skadi - Código de Recuperação de Senha");

//      Deixar o e-mail bonito
        String htmlContent = "<!DOCTYPE html>"
                + "<html>"
                + "<body style=\"font-family: Arial, sans-serif; background-color: #f4f6f9; padding: 40px; margin: 0;\">"
                + "  <div style=\"max-width: 500px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 10px rgba(0,0,0,0.08);\">"
                + "    <h2 style=\"color: #0f172a; text-align: center; margin-top: 0;\">Skadi Enterprise</h2>"
                + "    <p style=\"color: #333333;\">Olá!</p>"
                + "    <p style=\"color: #333333;\">O seu código para redefinir a senha é:</p>"
                + "    <div style=\"background-color: #f1f5f9; padding: 15px; text-align: center; font-size: 28px; font-weight: bold; letter-spacing: 5px; color: #0f172a; border-radius: 6px; margin: 25px 0; border: 2px dashed #cbd5e1;\">"
                + "      " + codigo
                + "    </div>"
                + "    <p style=\"color: #64748b; font-size: 13px; text-align: center;\">O código vale por 15 minutos. Se não solicitou esta alteração, ignore este e-mail com segurança.</p>"
                + "  </div>"
                + "</body>"
                + "</html>";

        message.setContent(htmlContent, "text/html; charset=utf-8");
        Transport.send(message);
        System.out.println("E-mail de recuperação enviado.");
    }

//  Mensagem da página "Contate-nos": vai para a equipe Skadi (CONTATO_DESTINO, ou o próprio EMAIL_USER)
    public static void enviarContato(String nome, String sobrenome, String empresa, String mensagem) throws MessagingException {
        String[] cred = credenciais();
        Session session = criarSessao(cred[0], cred[1]);

        String destino = config("CONTATO_DESTINO");
        if (destino == null || destino.isEmpty()) {
            destino = cred[0];
        }

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(cred[0]));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
        message.setSubject("Skadi - Novo contato de " + nome + " " + sobrenome, "UTF-8");

        String texto = "Nome: " + nome + " " + sobrenome + "\n"
                + "Empresa: " + empresa + "\n\n"
                + "Mensagem:\n" + mensagem + "\n";
        message.setText(texto, "UTF-8");

        Transport.send(message);
    }
}
