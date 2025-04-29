package com.ludicamente.Ludicamente.dto;

import java.util.Date;

public class NiñoDto {
    private Integer idNiño;
    private String nombre;
    private String sexo;
    private Date fechaNacimiento;
    private String observaciones;
    private String foto;
    private Integer idAcudiente;

    // Getters y setters
    // Constructor(es)

    public NiñoDto(Integer idNiño, String nombre, String sexo, Date fechaNacimiento, String observaciones, String foto, Integer idAcudiente) {
        this.idNiño = idNiño;
        this.nombre = nombre;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.observaciones = observaciones;
        this.foto = foto;
        this.idAcudiente = idAcudiente;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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