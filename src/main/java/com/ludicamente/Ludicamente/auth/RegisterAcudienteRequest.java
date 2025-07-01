package com.ludicamente.Ludicamente.auth;

import com.ludicamente.Ludicamente.dto.NiñoDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class RegisterAcudienteRequest {


    @NotBlank(message = "El nombre del acudiente es obligatorio")
    private String nombreAcudiente;
    @Email(message = "El correo debe ser válido")
    @NotBlank(message = "El correo es obligatorio")
    private String correoAcudiente;
    @NotBlank(message = "La contraseña es obligatoria")
    private String contraseñaAcudiente;
    @NotBlank(message = "La cédula es obligatoria")
    private String cedulaAcudiente;
    private String parentesco;
    private String telefonoAcudiente;
    @NotEmpty(message = "Debe registrar al menos un niño")
    private List<NiñoDto> niños;

    // Getters y Setters
    public String getCedulaAcudiente() {
        return cedulaAcudiente;
    }

    public void setCedulaAcudiente(String cedulaAcudiente) {
        this.cedulaAcudiente = cedulaAcudiente;
    }

    public String getNombreAcudiente() {
        return nombreAcudiente;
    }

    public void setNombreAcudiente(String nombreAcudiente) {
        this.nombreAcudiente = nombreAcudiente;
    }

    public String getCorreoAcudiente() {
        return correoAcudiente;
    }

    public void setCorreoAcudiente(String correoAcudiente) {
        this.correoAcudiente = correoAcudiente;
    }

    public String getContraseñaAcudiente() {
        return contraseñaAcudiente;
    }

    public void setContraseñaAcudiente(String contraseñaAcudiente) {
        this.contraseñaAcudiente = contraseñaAcudiente;
    }

    public String getTelefonoAcudiente() {
        return telefonoAcudiente;
    }

    public void setTelefonoAcudiente(String telefonoAcudiente) {
        this.telefonoAcudiente = telefonoAcudiente;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public List<NiñoDto> getNiños() {
        return niños;
    }

    public void setNiños(List<NiñoDto> niños) {
        this.niños = niños;
    }
}