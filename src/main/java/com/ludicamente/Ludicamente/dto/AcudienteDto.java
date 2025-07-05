package com.ludicamente.Ludicamente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AcudienteDto {

    private Integer idAcudiente;

    @NotBlank(message = "La cédula es obligatoria")
    @Size(max = 10, message = "La cédula no debe exceder los 10 dígitos")
    // Patron para asegurar que solo haya digitos y maximo 10
    @Pattern(regexp = "^\\d{1,10}$", message = "La cédula debe contener solo números y tener un máximo de 10 dígitos")
    private String cedula;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres")
    // Patron para asegurar que solo haya letras (incluyendo acentos y Ñ/ñ) y espacios
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]+$", message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @Email(message = "El correo electrónico no es válido")
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Size(max = 100, message = "El correo no puede exceder los 100 caracteres")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 255, message = "La contraseña debe tener entre 8 y 255 caracteres")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Para que no se devuelva en respuestas GET
    private String contraseña;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 10, message = "El teléfono no puede exceder los 10 dígitos")
    // Patron para asegurar que solo haya exactamente 10 digitos
    @Pattern(regexp = "^\\d{10}$", message = "El teléfono debe contener exactamente 10 dígitos")
    private String telefono;

    @NotBlank(message = "El parentesco es obligatorio")
    @Pattern(regexp = "padre|madre|abuela|abuelo|tio|tia|hermana|hermano|otro", message = "Parentesco no válido")
    private String parentesco;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 100, message = "La dirección no puede exceder los 100 caracteres")
    private String direccion;

    // Constructores (mantén los que ya tienes, no es necesario modificarlos para las validaciones)
    public AcudienteDto() {}

    public AcudienteDto(Integer idAcudiente, String cedula, String nombre, String correo, String contraseña, String telefono, String parentesco, String direccion) {
        this.idAcudiente = idAcudiente;
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.contraseña = contraseña;
        this.telefono = telefono;
        this.parentesco = parentesco;
        this.direccion = direccion;
    }

    public AcudienteDto(Integer idAcudiente, String cedula, String nombre, String correo, String telefono, String parentesco) {
        this.idAcudiente = idAcudiente;
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.parentesco = parentesco;
    }

    // Getters y Setters (sin cambios)
    public Integer getIdAcudiente() { return idAcudiente; }
    public void setIdAcudiente(Integer idAcudiente) { this.idAcudiente = idAcudiente; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContraseña() { return contraseña; }
    public void setContraseña(String contraseña) { this.contraseña = contraseña; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getParentesco() { return parentesco; }
    public void setParentesco(String parentesco) { this.parentesco = parentesco; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}