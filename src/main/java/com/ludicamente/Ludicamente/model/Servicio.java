package com.ludicamente.Ludicamente.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;

import java.math.BigDecimal;

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
    private BigDecimal costo;

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
    @Column(name = "estado", columnDefinition = "ENUM('DISPONIBLE', 'NO_DISPONIBLE') DEFAULT 'DISPONIBLE'")
    private EstadoServicio estado;

    public Servicio() {}

    public Servicio(String nombreServicio, String descripcion, BigDecimal  costo, int duracionMinutos,
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

    public BigDecimal  getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal  costo) {
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

    public enum EstadoServicio {
        disponible,
        no_disponible;

        @JsonCreator
        public static EstadoServicio fromString(String valor) {
            if (valor == null) {
                throw new IllegalArgumentException("El estado no puede ser nulo");
            }

            return switch (valor.trim().toLowerCase()) {
                case "disponible" -> disponible;
                case "no_disponible" -> no_disponible;
                default -> throw new IllegalArgumentException("EstadoServicio inválido: " + valor);
            };
        }

        @JsonValue
        public String toJson() {
            return name(); // devuelve "disponible" o "no_disponible"
        }
    }

}
