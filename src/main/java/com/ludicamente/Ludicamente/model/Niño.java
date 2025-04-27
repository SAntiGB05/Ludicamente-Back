package com.ludicamente.Ludicamente.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "niño")
public class Niño {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_niño")
    private Integer idNiño;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(length = 1)
    private String sexo; // 'M' o 'F'

    @Column(name = "fecha_nacimiento", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(length = 255)
    private String foto; // URL de la imagen

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_acudiente", nullable = false)
    private Acudiente acudiente;

    public Niño(){

    }

    public Niño(Integer idNiño, String nombre, String sexo, Date fechaNacimiento, String observaciones, String foto, Acudiente acudiente) {
        this.idNiño = idNiño;
        this.nombre = nombre;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.observaciones = observaciones;
        this.foto = foto;
        this.acudiente = acudiente;
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

    public Acudiente getAcudiente() {
        return acudiente;
    }

    public void setAcudiente(Acudiente acudiente) {
        this.acudiente = acudiente;
    }

    // Getters y setters
    // Constructor(es)
}