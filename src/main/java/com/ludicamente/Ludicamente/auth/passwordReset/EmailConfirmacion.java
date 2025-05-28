package com.ludicamente.Ludicamente.auth.passwordReset;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailConfirmacion {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreoBienvenida(String destinatario, String nombre) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(destinatario);
        helper.setSubject("🎉 ¡Bienvenida a Ludicamente, " + nombre + "! 🎉");
        helper.setFrom("sofi.hoyos.2509@gmail.com");

        String htmlContent = """
        <html>
        <head>
        <meta charset='UTF-8'>
        </head>
        <body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>
        <div style='max-width: 600px; margin: auto; background-color: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>
            <h2 style='color: #4CAF50;'>¡Hola " + nombre + "!</h2>
            <p style='font-size: 16px; color: #333;'>Gracias por registrarte en <strong>Lúdicamente</strong>.</p>
            <p style='font-size: 16px; color: #333;'>Ya puedes empezar a disfrutar de nuestra plataforma educativa.</p>
            <p style='font-size: 16px; color: #333;'>Si tienes dudas o necesitas ayuda, no dudes en escribirnos.</p>
            <br>
            <p style='font-size: 16px; color: #333;'>Con cariño,</p>
            <p style='font-size: 16px; color: #333;'>El equipo de <strong>Lúdicamente</strong></p>
        </div>
        </body>
        </html>
        """;



        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
