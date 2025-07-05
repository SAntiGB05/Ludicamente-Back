package com.ludicamente.Ludicamente.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "niño")
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
    private LocalDate fechaNacimiento;

    @Column(nullable = false)
    private Integer edad;

    @Column(name = "foto", columnDefinition = "TEXT")
    private String foto;

    @ManyToOne
    @JsonBackReference // evita ciclo infinito al serializar Acudiente → Niño → Acudiente...
    @JoinColumn(name = "fkid_acudiente", nullable = false)
    private Acudiente acudiente;

    @OneToMany(mappedBy = "niño", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference // emparejado con @JsonBackReference en Bitacora
    private List<Bitacora> bitacoras = new ArrayList<>();

    // === Constructores ===

    public Niño() {
    }

    public Niño(Integer idNiño) {
        this.idNiño = idNiño;
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

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
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

    public List<Bitacora> getBitacoras() {
        return bitacoras;
    }

    public void setBitacoras(List<Bitacora> bitacoras) {
        this.bitacoras = bitacoras;
    }
}
