package com.ludicamente.Ludicamente.model;

import jakarta.persistence.*;

@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_servicio")
    private int codServicio;

    @Column(name = "nombre_servicio", length = 50, nullable = false)
    private String nombreServicio;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "costo", nullable = false)
    private double costo;

    @Column(name = "duracion_minutos", nullable = false)
    private int duracionMinutos;

    @Column(name = "capacidad_maxima")
    private Integer capacidadMaxima;

    @ManyToOne
    @JoinColumn(name = "fkcod_categoria", nullable = false)
    private Categoria categoria;

    @Column(name = "requisitos")
    private String requisitos;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "ENUM('disponible', 'no_disponible') DEFAULT 'disponible'")
    private EstadoServicio estado;

    // Constructor vacío
    public Servicio() {}

    // Constructor con parámetros
    public Servicio(String nombreServicio, String descripcion, double costo, int duracionMinutos,
                    Integer capacidadMaxima, Categoria categoria, String requisitos, EstadoServicio estado) {
        this.nombreServicio = nombreServicio;
        this.descripcion = descripcion;
        this.costo = costo;
        this.duracionMinutos = duracionMinutos;
        this.capacidadMaxima = capacidadMaxima;
        this.categoria = categoria;
        this.requisitos = requisitos;
        this.estado = estado;
    }

    // Getters y Setters
    public int getCodServicio() {
        return codServicio;
    }

    public void setCodServicio(int codServicio) {
        this.codServicio = codServicio;
    }

    public String getNombreServicio() {
        return nombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        this.nombreServicio = nombreServicio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public EstadoServicio getEstado() {
        return estado;
    }

    public void setEstado(EstadoServicio estado) {
        this.estado = estado;
    }

    // Enum para el estado del servicio
    public enum EstadoServicio {
        DISPONIBLE,
        NO_DISPONIBLE
    }
}