package com.ludicamente.Ludicamente.auth.passwordReset;

import com.ludicamente.Ludicamente.dto.VerificationCodeData;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailServiceEmpleados {

    @Autowired
    private JavaMailSender mailSender;

    private final Map<String, VerificationCodeData> verificationCodes = new ConcurrentHashMap<>();

    // Correo de texto plano
    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        mensaje.setFrom("santigbttobi@gmail.com");
        mailSender.send(mensaje);
    }

    // Correo en formato HTML
    public void enviarCorreoHtml(String destinatario, String asunto, String contenidoHtml) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(contenidoHtml, true);
            helper.setFrom("santigbttobi@gmail.com");

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo HTML", e);
        }
    }

    // Enviar código de verificación en texto plano
    public void sendVerificationCode(String email) {
        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(1);

        verificationCodes.put(email, new VerificationCodeData(code, expirationTime));

        String contenidoHtml = String.format("""
        <html>
          <head>
            <meta charset="UTF-8">
            <title>Código de verificación</title>
          </head>
          <body style=" font-family: sans-serif; color: #000000; padding: 24px;">
            <div style="max-width: 500px;  margin: 0 auto; border-radius: 16px;   padding: 24px;">

              <div style="text-align: center; margin-bottom: 16px;">
                <img src='cid:logoLudicamente' alt='Logo Ludicamente' style='width: 100%%; height: 112px; object-fit: contain;' />
              </div>

              <h2 style="font-size: 20px; font-weight: 600;color: #000000; margin-bottom: 12px;">Tu código de verificación</h2>

              <p style="font-size: 14px; color: #000000; margin-bottom: 16px;">
                Usa el siguiente código para continuar con tu proceso de verificación:
              </p>

              <div style="font-size: 32px; font-weight: bold; text-align: center; color: #cd7e4e; letter-spacing: 4px; margin: 24px 0;">
                %s
              </div>

              <p style="font-size: 14px; color: #4b5563; text-align: center;">
                Este código es válido por <strong>1 minuto</strong>.<br />
                Si no solicitaste este código, puedes ignorar este mensaje.
              </p>
            </div>
          </body>
        </html>
    """, code);

        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("🔐 Código de verificación - Lúdicamente");
            helper.setText(contenidoHtml, true);
            helper.setFrom("santigbttobi@gmail.com");

            // Logo embebido (requiere tener el archivo en src/main/resources/static/)
            helper.addInline("logoLudicamente", new ClassPathResource("static/LogoCorreo.png"));

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el código de verificación", e);
        }
    }

    public boolean verifyCode(String email, String code) {
        VerificationCodeData data = verificationCodes.get(email);

        if (data == null) return false;
        if (LocalDateTime.now().isAfter(data.getExpirationTime())) {
            verificationCodes.remove(email);
            return false;
        }

        boolean isValid = data.getCode().equals(code);
        if (isValid) {
            verificationCodes.remove(email);
        }

        return isValid;
    }

    // Correo de reserva con estilo visual moderno
    public void enviarCorreoReservaExitosa(String destino, String nombreCliente, String codReserva) {
        String cancelLink = "https://tusitio.com/cancelar-reserva/" + codReserva;

        String contenidoHtml = String.format("""
            <html>
              <head>
                <meta charset="UTF-8">
                <title>Reserva Exitosa</title>
              </head>
              <body style="background-color: #fef3c7; font-family: sans-serif; color: #333333; padding: 24px;">
                <div style="max-width: 500px; margin: 0 auto; background-color: white; border-radius: 16px; border: 1px solid #d1d5db; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); padding: 24px;">

                  <div style="text-align: center; margin-bottom: 16px;">
                    <img src='cid:logoLudicamente' alt='Logo Ludicamente' style='width: 100%%; height: 130px; object-fit: contain;' />
                  </div>

                  <h2 style="font-size: 20px; font-weight: 600; margin-bottom: 8px;">¡Hola %s!</h2>
                  <p style="font-size: 14px; margin-bottom: 12px;">
                    Tu reserva ha sido <strong>procesada exitosamente</strong>.
                  </p>
                  <p style="font-size: 14px; margin-bottom: 12px;">
                    <strong>Código de reserva:</strong> <span style="color: #cd7e4e;">%s</span>
                  </p>

                  <div style="text-align: center; margin-bottom: 24px;">
                    <a href="%s" style="background-color: #cd7e4e; color: #fff8dc; padding: 12px 24px; border-radius: 9999px; font-size: 14px; font-weight: 600; text-decoration: none; display: inline-block;">
                      Cancelar mi reserva
                    </a>
                  </div>

                  <p style="font-size: 14px; color: #4b5563; text-align: center;">
                    Gracias por confiar en <strong>Lúdicamente</strong>.<br />
                    Si necesitas ayuda, contáctanos en cualquier momento.
                  </p>
                </div>
              </body>
            </html>
        """, nombreCliente, codReserva, cancelLink);

        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(destino);
            helper.setSubject("🎟️ ¡Reserva Exitosa en Lúdicamente!");
            helper.setFrom("santigbttobi@gmail.com");
            helper.setText(contenidoHtml, true);

            // Imagen embebida (debes tener LogoCorreo.png en src/main/resources/static/)
            helper.addInline("logoLudicamente", new ClassPathResource("static/LogoCorreo.png"));

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo de reserva", e);
        }
    }

    /**
     * Envía un correo de bienvenida a un nuevo empleado con sus credenciales.
     * @param destinatario El correo electrónico del empleado.
     * @param nombre El nombre del empleado.
     * @param correoEmpleado El correo con el que se registrará el empleado.
     * @param contrasenaAsignada La contraseña generada para el empleado.
     * @throws MessagingException Si ocurre un error al enviar el correo.
     */
    public void enviarCorreoBienvenidaEmpleado(String destinatario, String nombre, String correoEmpleado, String contrasenaAsignada) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(destinatario);
        helper.setSubject("👋 ¡Bienvenido a Lúdicamente, " + nombre + "! Tus credenciales de acceso");
        helper.setFrom("santigbttobi@gmail.com"); // Asegúrate de que este sea tu correo remitente

        String htmlContent = String.format("""
            <html>
              <head>
                <meta charset="UTF-8">
                <title>Bienvenida a Lúdicamente</title>
              </head>
              <body style="background-color: #fef3c7; font-family: sans-serif; color: #333333; padding: 24px;">
                <div style="max-width: 500px; margin: 0 auto; background-color: white; border-radius: 16px; border: 1px solid #d1d5db; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); padding: 24px;">

                  <div style="text-align: center; margin-bottom: 16px;">
                    <img src='cid:logoLudicamente' alt='Logo Ludicamente' style='width: 100%%; height: 112px; object-fit: contain;' />
                  </div>

                  <h2 style="font-size: 20px; font-weight: 600; margin-bottom: 8px;">¡Hola %s!</h2>
                  <p style="font-size: 14px; margin-bottom: 16px;">
                    Te damos la bienvenida al equipo de <strong>Lúdicamente</strong>. ¡Estamos encantados de tenerte con nosotros!
                  </p>
                  <p style="font-size: 14px; margin-bottom: 16px;">
                    Estas son tus credenciales para acceder a la plataforma:
                  </p>
                  <ul style="list-style: none; padding: 0; margin-bottom: 24px; border: 1px solid #eee; border-radius: 8px; padding: 16px; background-color: #f9f9f9;">
                    <li style="margin-bottom: 8px;"><strong>Correo:</strong> <span style="color: #cd7e4e;">%s</span></li>
                    <li><strong>Contraseña:</strong> <span style="color: #cd7e4e;">%s</span></li>
                  </ul>
                  <p style="font-size: 14px; margin-bottom: 16px;">
                    Te recomendamos cambiar tu contraseña una vez que inicies sesión por primera vez.
                  </p>

                  <div style="text-align: center; margin-bottom: 24px;">
                    <a href="https://ludicamente.onrender.com/" style="background-color: #cd7e4e; color: #fff8dc; padding: 12px 24px; border-radius: 9999px; font-size: 14px; font-weight: 600; text-decoration: none; display: inline-block;">
                      Iniciar Sesión
                    </a>
                  </div>

                  <p style="font-size: 14px; color: #4b5563; text-align: center;">
                    Si necesitas ayuda no dudes en escribirnos,<br />
                    <em>con cariño, el equipo de Lúdicamente</em>
                  </p>
                </div>
              </body>
            </html>
            """, nombre, correoEmpleado, contrasenaAsignada);

        helper.setText(htmlContent, true);

        // Adjuntar imágenes embebidas desde resources/static/
        helper.addInline("logoLudicamente", new ClassPathResource("static/LogoCorreo.png"));
        // Puedes agregar otra imagen si lo deseas, como una de bienvenida específica para empleados
        // helper.addInline("imagenBienvenidaEmpleado", new ClassPathResource("static/ImgBienvenidaEmpleado.png"));

        mailSender.send(message);
    }
}