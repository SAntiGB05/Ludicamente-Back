package com.ludicamente.Ludicamente.auth;

import java.math.BigDecimal;
import java.time.LocalDate; // ¡Importante! Usaremos LocalDate

public class RegisterEmpleadoRequest {

    private String cedulaEmpleado;
    private String nombreEmpleado;
    private String correoEmpleado;
    private String contraseñaEmpleado;
    private String telefonoEmpleado;
    private String direccionEmpleado;
    private LocalDate fechaContratacion; // ¡CORRECCIÓN! Cambiado a LocalDate y nombre corregido
    private BigDecimal salario;
    private String horario;
    private Integer nivelAcceso;

    // Puedes añadir un constructor si lo deseas, o depender del constructor por defecto.

    // Getters y Setters
    public String getCedulaEmpleado() {
        return cedulaEmpleado;
    }

    public void setCedulaEmpleado(String cedulaEmpleado) {
        this.cedulaEmpleado = cedulaEmpleado;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public String getCorreoEmpleado() {
        return correoEmpleado;
    }

    public void setCorreoEmpleado(String correoEmpleado) {
        this.correoEmpleado = correoEmpleado;
    }

    public String getContraseñaEmpleado() {
        return contraseñaEmpleado;
    }

    public void setContraseñaEmpleado(String contraseñaEmpleado) {
        this.contraseñaEmpleado = contraseñaEmpleado;
    }

    public String getTelefonoEmpleado() {
        return telefonoEmpleado;
    }

    public void setTelefonoEmpleado(String telefonoEmpleado) {
        this.telefonoEmpleado = telefonoEmpleado;
    }

    public String getDireccionEmpleado() {
        return direccionEmpleado;
    }

    public void setDireccionEmpleado(String direccionEmpleado) {
        this.direccionEmpleado = direccionEmpleado;
    }

    public LocalDate getFechaContratacion() { // ¡CORRECCIÓN! Nombre del getter y tipo de retorno
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) { // ¡CORRECCIÓN! Nombre del setter y tipo de parámetro
        this.fechaContratacion = fechaContratacion;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Integer getNivelAcceso() {
        return nivelAcceso;
    }

    public void setNivelAcceso(Integer nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }
}