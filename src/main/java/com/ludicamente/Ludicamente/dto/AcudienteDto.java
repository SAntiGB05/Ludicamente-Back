package com.ludicamente.Ludicamente.dto;

public class AcudienteDto {
    private Integer idAcudiente;
    private String cedula;
    private String nombre;
    private String correo;
    private String contraseña;
    private String telefono;
    private String parentesco;
    private String direccion;


    public AcudienteDto(Integer idAcudiente, String direccion, String parentesco, String telefono, String contraseña, String correo) {
        this.idAcudiente = idAcudiente;
        this.direccion = direccion;
        this.parentesco = parentesco;
        this.telefono = telefono;
        this.contraseña = contraseña;
        this.correo = correo;
        this.nombre = nombre;
        this.cedula = cedula;
    }
    // En AcudienteDto.java
    public AcudienteDto(Integer idAcudiente, String nombre, String cedula) {
        this.idAcudiente = idAcudiente;
        this.nombre = nombre;
        this.cedula = cedula;
    }


    public AcudienteDto() {
    }

    public Integer getIdAcudiente() {
        return idAcudiente;
    }

    public void setIdAcudiente(Integer idAcudiente) {
        this.idAcudiente = idAcudiente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
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

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
}