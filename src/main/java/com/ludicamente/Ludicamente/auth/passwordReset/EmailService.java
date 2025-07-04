package com.ludicamente.Ludicamente.auth.passwordReset;

import com.ludicamente.Ludicamente.dto.VerificationCodeData;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    // En emailService
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

    // Correo en formato HTML (para botones, estilos, etc.)
    public void enviarCorreoHtml(String destinatario, String asunto, String contenidoHtml) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(contenidoHtml, true); // true = HTML
            helper.setFrom("santigbttobi@gmail.com");

            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo HTML", e);
        }
    }



    public void sendVerificationCode(String email) {
        String code = String.valueOf(new Random().nextInt(900000) + 100000);
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(1);

        verificationCodes.put(email, new VerificationCodeData(code, expirationTime));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Código de verificación");
        message.setText("Tu código de verificación es: " + code);

        mailSender.send(message);
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

    public void enviarCorreoReservaExitosa(String destino, String nombreCliente, String codReserva) {
        String cancelLink = "https://tusitio.com/cancelar-reserva/" + codReserva;

        String contenidoHtml = """
        <div style="font-family: Arial, sans-serif; padding: 20px;">
            <h2 style="color: #A35E5E;">¡Hola %s!</h2>
            <p>Tu reserva ha sido <strong>procesada exitosamente</strong>.</p>
            <p><strong>Código de reserva:</strong> %s</p>
            <p>Si deseas cancelar tu reserva, puedes hacerlo desde tu perfil o haciendo clic en el siguiente botón:</p>
            <a href="%s" style="display: inline-block; padding: 10px 20px; background-color: #F76C6C; color: white; text-decoration: none; border-radius: 5px;">Cancelar Reserva</a>
            <p style="margin-top: 20px;">Gracias por confiar en <strong>Ludicamente</strong>.</p>
        </div>
    """.formatted(nombreCliente, codReserva, cancelLink);

        enviarCorreoHtml(destino, "Reserva Exitosa en Ludicamente", contenidoHtml);
    }


}
