// Archivo: src/main/java/com/ludicamente/Ludicamente/model/MessageResponse.java
package com.ludicamente.Ludicamente.model;

public class MessageResponse {
    private String message;
    private boolean success; // Opcional: para indicar explícitamente el éxito/fracaso

    public MessageResponse(String message) {
        this.message = message;
        this.success = true; // Por defecto exitoso, puedes cambiarlo si es un error
    }

    public MessageResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    // Getters y Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}