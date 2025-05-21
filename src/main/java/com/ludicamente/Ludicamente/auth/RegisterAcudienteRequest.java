package com.ludicamente.Ludicamente.auth;

import com.ludicamente.Ludicamente.dto.NiñoDto;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class RegisterAcudienteRequest {

    private String cedulaAcudiente;
    private String nombreAcudiente;
    private String correoAcudiente;
    private String contraseñaAcudiente;
    private String telefonoAcudiente;
    private String parentesco;

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
