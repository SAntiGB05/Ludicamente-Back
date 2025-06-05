package com.ludicamente.Ludicamente.dto;

import java.util.Date;

public class NiñoDto {

    private Integer idNiño;
    private String nombre;
    private String nIdentificacion;
    private String sexo;
    private Date fechaNacimiento;
    private Integer edad;
    private String foto;
    private Integer idAcudiente;

    // === Constructores ===

    public NiñoDto() {
    }

    public NiñoDto(Integer idNiño, String nombre, String nIdentificacion, String sexo, Date fechaNacimiento, Integer edad, String foto, Integer idAcudiente) {
        this.idNiño = idNiño;
        this.nombre = nombre;
        this.nIdentificacion = nIdentificacion;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.edad = edad;
        this.foto = foto;
        this.idAcudiente = idAcudiente;
    }

    // === Getters y Setters ===

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
}
