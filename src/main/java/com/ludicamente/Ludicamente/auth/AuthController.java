package com.ludicamente.Ludicamente.auth;

import com.ludicamente.Ludicamente.auth.passwordReset.EmailConfirmacion;
import com.ludicamente.Ludicamente.auth.passwordReset.EmailService;
import com.ludicamente.Ludicamente.auth.passwordReset.PasswordResetService;
import com.ludicamente.Ludicamente.dto.EmailRequest;
import com.ludicamente.Ludicamente.dto.VerificationRequest;
import com.ludicamente.Ludicamente.model.PasswordResetToken;
import com.ludicamente.Ludicamente.auth.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // URL base para autenticación
public class AuthController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailConfirmacion emailConfirmacion; // Inyección de EmailConfirmacion
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    // Endpoint para iniciar sesión
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        try {
            AuthResponse response = authenticationService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AuthResponse("Correo no registrado"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("Contraseña incorrecta"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse("Error en la autenticación: " + e.getMessage()));
        }
    }

    // Endpoint para registrar un Acudiente
    @PostMapping("/register/acudiente")
    public ResponseEntity<?> registerAcudiente(@RequestBody @Valid RegisterAcudienteRequest request, BindingResult result) {
        if (result.hasErrors()) {
            // Maneja los errores de validación de la solicitud
            StringBuilder errorMessage = new StringBuilder();
            for (ObjectError error : result.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append(" ");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
        }

        try {
            // Registrar al acudiente
            AuthResponse response = authenticationService.registerAcudiente(request);

            // Enviar correo de bienvenida después del registro exitoso
            emailConfirmacion.enviarCorreoBienvenida(request.getCorreoAcudiente(), request.getNombreAcudiente());

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Maneja el error cuando ya existe un correo
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(e.getMessage()));
        } catch (MessagingException e) {
            // Maneja el error si el envío del correo falla
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al enviar el correo de bienvenida: " + e.getMessage());
        }
    }

    // Endpoint para registrar un Empleado
    @PostMapping("/register/empleado")
    public ResponseEntity<?> registerEmpleado(@RequestBody @Valid RegisterEmpleadoRequest request, BindingResult result) {
        if (result.hasErrors()) {
            // Maneja los errores de validación de la solicitud
            StringBuilder errorMessage = new StringBuilder();
            for (ObjectError error : result.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append(" ");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage.toString());
        }

        try {
            return ResponseEntity.ok(authenticationService.registerEmpleado(request));
        } catch (IllegalArgumentException e) {
            // Maneja el error cuando ya existe un correo
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(e.getMessage()));
        }
    }

    // Endpoint para recuperar contraseña
    @Autowired
    private PasswordResetService passwordResetService;

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        PasswordResetToken token = passwordResetService.enviarToken(email);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Si el correo está registrado, recibirás un enlace para restablecer tu contraseña.");
        response.put("token", token.getToken()); // Solo para desarrollo

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        passwordResetService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Contraseña restablecida correctamente.");
    }

    @PostMapping("/send-verification-email")
    public ResponseEntity<?> sendVerification(@RequestBody EmailRequest request) {
        emailService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody VerificationRequest req) {
        boolean valid = emailService.verifyCode(req.getEmail(), req.getCode());
        if (valid) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Código inválido o expirado");
        }
    }
}