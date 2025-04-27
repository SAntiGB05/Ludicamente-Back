package com.ludicamente.Ludicamente.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.List;

@Entity
@Table(name = "acudiente")
public class Acudiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_acudiente")
    private Integer idAcudiente;

    @Column(name = "cedula_acudiente", nullable = false, length = 20, unique = true)
    private String cedula;

    @Column(name = "nombre_acudiente", length = 50)
    private String nombre;

    @Column(name = "correo_acudiente", length = 100, unique = true)
    private String correo;

    @Column(name = "contraseña_acudiente", length = 255)
    private String contraseña;

    @Pattern(regexp = "\\d{7,20}")
    @Column(name = "telefono_acudiente", length = 20)
    private String telefono;

    @Column(length = 50)
    private String parentesco;

    @Column(name = "direccion_acudiente", length = 50)
    private String direccion;

    @OneToMany(mappedBy = "acudiente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Niño> niños;

    public Acudiente(){

    }

    public Acudiente(Integer idAcudiente, List<Niño> niños, String direccion, String parentesco, String contraseña, String telefono, String correo, String nombre, String cedula) {
        this.idAcudiente = idAcudiente;
        this.niños = niños;
        this.direccion = direccion;
        this.parentesco = parentesco;
        this.contraseña = contraseña;
        this.telefono = telefono;
        this.correo = correo;
        this.nombre = nombre;
        this.cedula = cedula;
    }

    public Integer getIdAcudiente() {
        return idAcudiente;
    }

    public void setIdAcudiente(Integer idAcudiente) {
        this.idAcudiente = idAcudiente;
    }

    public List<Niño> getNiños() {
        return niños;
    }

    public void setNiños(List<Niño> niños) {
        this.niños = niños;
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

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }
}