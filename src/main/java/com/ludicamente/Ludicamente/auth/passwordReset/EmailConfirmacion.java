package com.ludicamente.Ludicamente.auth.passwordReset;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
        helper.setSubject("🎉 ¡Bienvenida a Lúdicamente, " + nombre + "! 🎉");
        helper.setFrom("santigbttobi@gmail.com");

        String htmlContent = String.format("""
            <html>
              <head>
                <meta charset="UTF-8">
                <title>Bienvenida a Lúdicamente</title>
              </head>
              <body style="background-color: #fef3c7; font-family: sans-serif; color: #333333; padding: 24px;">
                <div style="max-width: 500px; margin: 0 auto; background-color: white; border-radius: 16px; border: 1px solid #d1d5db; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); padding: 24px;">

                  <!-- Encabezado con logo -->
                  <div style="text-align: center; margin-bottom: 16px;">
                    <img src='cid:logoLudicamente' alt='Logo Ludicamente' style='width: 100%%; height: 112px; object-fit: contain;' />
                  </div>

                  <!-- Texto de saludo -->
                  <h2 style="font-size: 20px; font-weight: 600; margin-bottom: 8px;">¡Hola %s!</h2>
                  <p style="font-size: 14px; margin-bottom: 16px;">
                    Gracias por registrarte en <strong>Lúdicamente</strong>.<br />
                    Ya puedes empezar a disfrutar de nuestra plataforma educativa.
                  </p>

                  <!-- Imagen central -->
                  <div style="text-align: center; margin-bottom: 16px;">
                    <img src='cid:imagenLudicamente' alt='Imagen Ludicamente' style='width: 100%%; border-radius: 12px;' />
                  </div>

                  <!-- Botón CTA -->
                  <div style="text-align: center; margin-bottom: 24px;">
                    <a href="https://ludicamente.onrender.com/" style="background-color: #cd7e4e; color: #fff8dc; padding: 12px 24px; border-radius: 9999px; font-size: 14px; font-weight: 600; text-decoration: none; display: inline-block;">
                      Comenzar mi aventura
                    </a>
                  </div>

                  <!-- Mensaje final -->
                  <p style="font-size: 14px; color: #4b5563; text-align: center;">
                    Si necesitas ayuda no dudes en escribirnos,<br />
                    <em>con cariño, el equipo de Lúdicamente</em>
                  </p>
                </div>
              </body>
            </html>
            """, nombre);

        helper.setText(htmlContent, true);

        // Adjuntar imágenes embebidas desde resources/static/
        helper.addInline("logoLudicamente", new ClassPathResource("static/LogoCorreo.png"));
        helper.addInline("imagenLudicamente", new ClassPathResource("static/ImgCorreo.jpeg"));

        mailSender.send(message);
    }
}
