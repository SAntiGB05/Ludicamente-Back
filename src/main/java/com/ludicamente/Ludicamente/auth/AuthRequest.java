package com.ludicamente.Ludicamente.auth;

//import jakarta.validation.constraints.*;

public class AuthRequest {

    // credenciales de inicio de sesión

  //  @Email(message = "El correo debe tener un formato válido")
    //@NotBlank(message = "El correo no puede estar vacío")
    private String email;

    //@NotBlank(message = "La contraseña no puede estar vacía")
    //@Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    public AuthRequest(){

    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    // Añadir setters si es necesario (por ejemplo, para pruebas o frameworks)
    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
