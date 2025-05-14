package com.ludicamente.Ludicamente.auth;

public class AuthResponse {

    // Contiene el JWT que se devuelve al usuario
    private String token;

    // Constructor para asignar el token
    public AuthResponse(String jwtToken) {
        this.token = jwtToken;
    }

    // Getter para obtener el token
    public String getToken() {
        return token;
    }

    // Setter para modificar el token (si es necesario)
    public void setToken(String token) {
        this.token = token;
    }
}
