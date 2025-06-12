package com.ludicamente.Ludicamente.auth.passwordReset;

import com.ludicamente.Ludicamente.model.Acudiente;
import com.ludicamente.Ludicamente.model.Empleado;
import com.ludicamente.Ludicamente.model.PasswordResetToken;
import com.ludicamente.Ludicamente.repository.AcudienteRepository;
import com.ludicamente.Ludicamente.repository.EmpleadoRepository;
import com.ludicamente.Ludicamente.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private AcudienteRepository acudienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public PasswordResetToken enviarToken(String email) {
        Optional<Empleado> empleadoOpt = empleadoRepository.findByCorreo(email);
        Optional<Acudiente> acudienteOpt = acudienteRepository.findByCorreo(email);

        if (empleadoOpt.isEmpty() && acudienteOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado con ese correo.");
        }

        PasswordResetToken token = new PasswordResetToken();
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        if (empleadoOpt.isPresent()) {
            Empleado empleado = empleadoOpt.get();
            token.setUserId(empleado.getIdEmpleado());
            token.setNivelAcceso(empleado.getNivelAcceso()); // O simplemente poner 1, 2, etc.
        } else {
            Acudiente acudiente = acudienteOpt.get();
            token.setUserId(acudiente.getIdAcudiente());
            token.setNivelAcceso(null); // Importante: dejarlo en null para identificar como Acudiente
        }

        tokenRepository.save(token);


        // Crear el enlace
        String link = "http://localhost:5173/resetPassword?token=" + token.getToken();

        // Crear HTML para el botón
        String mensajeHtml = """
            <html>
              <body style="font-family: Arial, sans-serif;">
                <p>Hola,</p>
                <p>Haz clic en el siguiente botón para restablecer tu contraseña:</p>
                <a href="%s" style="
                    display: inline-block;
                    padding: 10px 20px;
                    margin-top: 10px;
                    background-color: #cd7e4e;
                    color: white;
                    text-decoration: none;
                    border-radius: 5px;
                    font-weight: bold;
                ">
                  Cambiar contraseña
                </a>
                <p style="margin-top:20px;">Si no solicitaste este cambio, puedes ignorar este mensaje.</p>
              </body>
            </html>
        """.formatted(link);

        // Enviar el correo con formato HTML
        emailService.enviarCorreoHtml(email, "Restablece tu contraseña", mensajeHtml);

        return token;

    }

    public void resetPassword(String tokenStr, String nuevaClave) {
        PasswordResetToken token = tokenRepository.findByToken(tokenStr);
        if (token == null || token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token inválido o expirado.");
        }

        if (token.getNivelAcceso() != null) {
            Empleado empleado = empleadoRepository.findById(token.getUserId())
                    .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
            empleado.setContraseña(passwordEncoder.encode(nuevaClave));
            empleadoRepository.save(empleado);
        } else {
            Acudiente acudiente = acudienteRepository.findById(token.getUserId())
                    .orElseThrow(() -> new RuntimeException("Acudiente no encontrado"));
            acudiente.setContraseña(passwordEncoder.encode(nuevaClave));
            acudienteRepository.save(acudiente);
        }

        tokenRepository.delete(token);
    }
}
