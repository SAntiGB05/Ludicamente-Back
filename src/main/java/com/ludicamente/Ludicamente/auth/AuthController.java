package com.ludicamente.Ludicamente.auth;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth") // URL base para autenticación
public class AuthController {

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
            return ResponseEntity.ok(authenticationService.registerAcudiente(request));
        } catch (IllegalArgumentException e) {
            // Maneja el error cuando ya existe un correo
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse(e.getMessage()));
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
}
