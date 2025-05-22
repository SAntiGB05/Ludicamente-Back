package com.ludicamente.Ludicamente.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Niño {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNiño;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(name = "n_identificacion", nullable = false, unique = true)
    private String nIdentificacion;

    @Column(nullable = false, length = 10)
    private String sexo;

    @Temporal(TemporalType.DATE)
    @Column(name = "fecha_nacimiento", nullable = false)
    private Date fechaNacimiento;

    @Column(nullable = false)
    private Integer edad;

    private String foto;

    @ManyToOne
    @JoinColumn(name = "fkid_acudiente", nullable = false)
    private Acudiente acudiente;

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

    public Acudiente getAcudiente() {
        return acudiente;
    }

    public void setAcudiente(Acudiente acudiente) {
        this.acudiente = acudiente;
    }
}
