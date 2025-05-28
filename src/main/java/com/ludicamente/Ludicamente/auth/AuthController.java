package com.ludicamente.Ludicamente.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // URL base para autenticación
public class AuthController {

    private final AuthenticationService authenticationService;
    private final GoogleTokenVerifierService googleTokenVerifierService;

    @Autowired
    public AuthController(AuthenticationService authenticationService,
                          GoogleTokenVerifierService googleTokenVerifierService) {
        this.authenticationService = authenticationService;
        this.googleTokenVerifierService = googleTokenVerifierService;
    }

    // ... (tus endpoints previos)

    // Nuevo endpoint para autenticación con Google
    @PostMapping("/google")
    public ResponseEntity<?> authenticateWithGoogle(@RequestBody Map<String, String> request) {
        String idTokenString = request.get("idToken");
        if (idTokenString == null || idTokenString.isEmpty()) {
            return ResponseEntity.badRequest().body(new AuthResponse("idToken es obligatorio"));
        }

        try {
            Payload payload = googleTokenVerifierService.verify(idTokenString);
            if (payload == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse("Token inválido o expirado"));
            }

            String email = payload.getEmail();

            // Aquí intentamos autenticar al usuario usando su email
            // (Esto depende de cómo tengas implementada la autenticación por email)
            AuthRequest authRequest = new AuthRequest();
            authRequest.setEmail(email);
            authRequest.setPassword(null); // No tenemos contraseña al venir de Google, solo validamos existencia

            // Aquí tu lógica para buscar si el usuario ya existe (por ejemplo, método en AuthenticationService)
            boolean userExists = authenticationService.checkIfUserExistsByEmail(email);
            if (!userExists) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new AuthResponse("Usuario no registrado. No se puede iniciar sesión con Google."));
            }

            // Opcional: generar token JWT o lo que uses para la sesión
            AuthResponse response = authenticationService.authenticateWithGoogle(email);

            return ResponseEntity.ok(response);

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse("Error validando token de Google: " + e.getMessage()));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AuthResponse("Usuario no encontrado"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse("No autorizado"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponse("Error en la autenticación: " + e.getMessage()));
        }
    }
}
