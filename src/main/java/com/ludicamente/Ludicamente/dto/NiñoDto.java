package com.ludicamente.Ludicamente.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.Period;
import java.util.Date;

public class NiñoDto {

    private Integer idNiño;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre debe tener como máximo 50 caracteres")
    private String nombre;

    @NotBlank(message = "El número de identificación es obligatorio")
    private String nIdentificacion;

    @NotBlank(message = "El sexo es obligatorio")
    private String sexo;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private Date fechaNacimiento;

    @Min(value = 0, message = "La edad debe ser positiva")
    private Integer edad;

    @Column(name = "foto", columnDefinition = "TEXT") // o LONGTEXT si esperas imágenes más grandes
    private String foto;

    @NotNull(message = "Debe estar asociado a un acudiente")
    private Integer idAcudiente;
    private String cedulaAcudiente; // <--- ¡NUEVO CAMPO!
    private String  nombreAcudiente;
    private String parentescoAcudiente;
    private String telefonoAcudiente;
    private Boolean bitacoraActiva;


    public NiñoDto() {
    }

    public NiñoDto(Integer idNiño, String nombre, String nIdentificacion, String sexo, Date fechaNacimiento, Integer edad, String foto, Integer idAcudiente) { // <--- Constructor actualizado
        this.idNiño = idNiño;
        this.nombre = nombre;
        this.nIdentificacion = nIdentificacion;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.foto = foto;
        this.idAcudiente = idAcudiente;
        //this.cedulaAcudiente = cedulaAcudiente; // <--- Asignar nuevo campo

    }

    // === Getters y Setters ===

    public Boolean getBitacoraActiva() {
        return bitacoraActiva;
    }

    public void setBitacoraActiva(Boolean bitacoraActiva) {
        this.bitacoraActiva = bitacoraActiva;
    }
    public String getNombreAcudiente() {
        return nombreAcudiente;
    }

    public void setNombreAcudiente(String nombreAcudiente) {
        this.nombreAcudiente = nombreAcudiente;
    }

    public String getParentescoAcudiente() {
        return parentescoAcudiente;
    }

    public void setParentescoAcudiente(String parentescoAcudiente) {
        this.parentescoAcudiente = parentescoAcudiente;
    }

    public String getTelefonoAcudiente() {
        return telefonoAcudiente;
    }

    public void setTelefonoAcudiente(String telefonoAcudiente) {
        this.telefonoAcudiente = telefonoAcudiente;
    }

    public Integer getIdNiño() {
        return idNiño;
    }

    public void setIdNiño(Integer idNiño) {
        this.idNiño = idNiño;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getnIdentificacion() {
        return nIdentificacion;
    }

    public void setnIdentificacion(String nIdentificacion) {
        this.nIdentificacion = nIdentificacion;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Integer getIdAcudiente() {
        return idAcudiente;
    }

    public void setIdAcudiente(Integer idAcudiente) {
        this.idAcudiente = idAcudiente;
    }

    public String getCedulaAcudiente() { // <--- ¡NUEVO GETTER!
        return cedulaAcudiente;
    }

    public void setCedulaAcudiente(String cedulaAcudiente) { // <--- ¡NUEVO SETTER!
        this.cedulaAcudiente = cedulaAcudiente;
    }
}