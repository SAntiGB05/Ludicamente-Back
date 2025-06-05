package com.ludicamente.Ludicamente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmpleadoDto {


    private Integer idEmpleado;

    @NotBlank(message = "La cédula es obligatoria")
    private String cedulaEmpleado;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Email(message = "Correo inválido")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    private String direccion;

    @NotNull(message = "La fecha de contratación es obligatoria")
    private LocalDate fechaContratacion;

    @NotNull(message = "El salario es obligatorio")
    private BigDecimal salario;

    private String horario;

    private Integer nivelAcceso;

    // Constructor vacío
    public EmpleadoDto() {}

    // Constructor con parámetros (opcional)
    public EmpleadoDto(Integer idEmpleado, String cedulaEmpleado, String nombre, String correo,
                       String telefono, String direccion, LocalDate fechaContratacion,
                       BigDecimal salario, String horario, Integer nivelAcceso) {
        this.idEmpleado = idEmpleado;
        this.cedulaEmpleado = cedulaEmpleado;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.direccion = direccion;
        this.fechaContratacion = fechaContratacion;
        this.salario = salario;
        this.horario = horario;
        this.nivelAcceso = nivelAcceso;
    }

    // Getters y Setters
    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getCedulaEmpleado() {
        return cedulaEmpleado;
    }

    public void setCedulaEmpleado(String cedulaEmpleado) {
        this.cedulaEmpleado = cedulaEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDate getFechaContratacion() {
        return fechaContratacion;
    }

    public void setFechaContratacion(LocalDate fechaContratacion) {
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

