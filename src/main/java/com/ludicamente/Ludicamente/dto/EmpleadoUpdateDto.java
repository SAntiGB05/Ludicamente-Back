package com.ludicamente.Ludicamente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmpleadoUpdateDto {

    @NotBlank(message = "La cédula es obligatoria")
    @Size(max = 10, message = "La cédula no debe exceder los 10 dígitos")
    @Pattern(regexp = "^\\d{1,10}$", message = "La cédula debe contener solo números y tener un máximo de 10 dígitos")
    private String cedulaEmpleado;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @Email(message = "El correo electrónico no es válido")
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Size(max = 100, message = "El correo no puede exceder los 100 caracteres")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 15, message = "El teléfono no puede exceder los 15 caracteres")
    @Pattern(regexp = "^\\d{7,15}$", message = "El teléfono debe contener solo dígitos numéricos (entre 7 y 15)")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 100, message = "La dirección no puede exceder los 100 caracteres")
    private String direccion;

    @NotNull(message = "La fecha de contratación es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaContratacion;

    @NotNull(message = "El salario es obligatorio")
    @Min(value = 0, message = "El salario no puede ser negativo")
    // ELIMINAR O COMENTAR LA LÍNEA SIGUIENTE:
    // @Pattern(regexp = "^\\d+(\\.\\d{1,2})?$", message = "El salario debe ser un número válido (ej. 1000.00 o 1500)")
    private BigDecimal salario;

    @NotBlank(message = "El horario es obligatorio")
    @Size(max = 100, message = "El horario no puede exceder los 100 caracteres")
    private String horario;

    @NotNull(message = "El nivel de acceso es obligatorio")
    @Min(value = 1, message = "El nivel de acceso debe ser 1 o 2")
    @Max(value = 2, message = "El nivel de acceso debe ser 1 o 2")
    private Integer nivelAcceso;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "activo|inactivo", message = "El estado debe ser 'activo' o 'inactivo'")
    private String estado;

    // ... (rest of your class)
    public EmpleadoUpdateDto() {}
    public String getCedulaEmpleado() { return cedulaEmpleado; }
    public void setCedulaEmpleado(String cedulaEmpleado) { this.cedulaEmpleado = cedulaEmpleado; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public LocalDate getFechaContratacion() { return fechaContratacion; }
    public void setFechaContratacion(LocalDate fechaContratacion) { this.fechaContratacion = fechaContratacion; }
    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }
    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
    public Integer getNivelAcceso() { return nivelAcceso; }
    public void setNivelAcceso(Integer nivelAcceso) { this.nivelAcceso = nivelAcceso; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}