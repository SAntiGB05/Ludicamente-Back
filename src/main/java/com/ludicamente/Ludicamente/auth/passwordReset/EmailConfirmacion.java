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

        String htmlContent = "<html><body>" +
                "<h2>¡Hola " + nombre + "!</h2>" +
                "<p>Gracias por registrarte en <strong>Ludicamente</strong>.</p>" +
                "<p>Ya puedes empezar a disfrutar de nuestra plataforma educativa.</p>" +
                "<p>Si tienes dudas o necesitas ayuda, no dudes en escribirnos.</p>" +
                "<br>" +
                "<p>Con cariño,</p>" +
                "<p>El equipo de Ludicamente</p>" +
                "<p><a href='https://ludicamente.com'>www.ludicamente.com</a></p>" +
                "</body></html>";

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
