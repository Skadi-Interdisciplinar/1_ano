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
            message.setText("Olá!\n\nSeu código para redefinir a senha é: " + codigo + "\n\nSe você não solicitou isso, ignore este e-mail.");

            Transport.send(message);
            System.out.println("E-mail enviado para: " + destinatario);
        } catch (MessagingException e) {
            System.out.println("Erro ao enviar e-mail: " + e.getMessage());
        }
    }
}