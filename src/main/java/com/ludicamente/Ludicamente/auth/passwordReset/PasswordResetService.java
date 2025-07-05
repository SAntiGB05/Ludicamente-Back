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
            token.setNivelAcceso(empleado.getNivelAcceso());
        } else {
            Acudiente acudiente = acudienteOpt.get();
            token.setUserId(acudiente.getIdAcudiente());
            token.setNivelAcceso(null);
        }

        tokenRepository.save(token);

        String link = "http://localhost:5173/resetPassword?token=" + token.getToken();

        String mensajeHtml = String.format("""
            <html>
              <head>
                <meta charset="UTF-8">
                <title>Restablecer contraseña</title>
              </head>
              <body style="background-color: #fef3c7; font-family: sans-serif; color: #333333; padding: 24px;">
                <div style="max-width: 500px; margin: 0 auto; background-color: white; border-radius: 16px; border: 1px solid #d1d5db; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); padding: 24px;">

                
                  <h2 style="font-size: 20px; font-weight: 600; margin-bottom: 12px;">Restablece tu contraseña</h2>
                  <p style="font-size: 14px; margin-bottom: 16px;">
                    Hemos recibido una solicitud para restablecer tu contraseña. Si fuiste tú, haz clic en el siguiente botón:
                  </p>

                  <div style="text-align: center; margin-bottom: 24px;">
                    <a href="%s" style="background-color: #cd7e4e; color: #fff8dc; padding: 12px 24px; border-radius: 9999px; font-size: 14px; font-weight: 600; text-decoration: none; display: inline-block;">
                      Cambiar contraseña
                    </a>
                  </div>

                  <p style="font-size: 14px; color: #4b5563; text-align: center;">
                    Si no solicitaste este cambio, puedes ignorar este mensaje.<br />
                    Este enlace es válido por 30 minutos.
                  </p>

                </div>
              </body>
            </html>
        """, link);



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
