package com.example.skadi_beckend.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailUtil {
//  Enviar codigo
    public static void enviarCodigoRecuperacao(String destinatario, String codigo) {
//      Configurações do servidor SMTP do Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

//      Busca na VM
        String emailTemporario = System.getProperty("EMAIL_USER");
        String senhaTemporaria = System.getProperty("EMAIL_PASS");

//       Se não achou na VM, busca no .env
        if (emailTemporario == null || senhaTemporaria == null) {
            emailTemporario = System.getenv("EMAIL_USER");
            senhaTemporaria = System.getenv("EMAIL_PASS");
        }

//      Trava de segurança
        if (emailTemporario == null || senhaTemporaria == null) {
            System.out.println("ERRO: Credenciais de e-mail (EMAIL_USER / EMAIL_PASS) não foram encontradas!");
            return;
        }

        final String meuEmail = emailTemporario;
        final String minhaSenha = senhaTemporaria;

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(meuEmail, minhaSenha);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(meuEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Skadi - Código de Recuperação de Senha");

//          Deixar o e-mail bonito
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
                    + "    <p style=\"color: #64748b; font-size: 13px; text-align: center;\">Se não solicitou esta alteração, ignore este e-mail com segurança.</p>"
                    + "  </div>"
                    + "</body>"
                    + "</html>";

            // Define que o conteúdo é HTML
            message.setContent(htmlContent, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("E-mail enviado para: " + destinatario);
        } catch (MessagingException e) {
            System.out.println("Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}