package com.ludicamente.Ludicamente.dto;

import java.math.BigDecimal;
public class ServicioDto {

    private Integer codServicio;
    private String nombreServicio;
    private String descripcion;
    private BigDecimal costo;
    private Integer duracionMinutos;
    private Integer capacidadMaxima;
    private Integer fkcodCategoria;
    private String requisitos;
    private String estado;
    private String imageUrl; // <-- ¡NUEVO CAMPO!
    public ServicioDto() {}

    public ServicioDto(Integer codServicio, String nombreServicio, String descripcion,
                       BigDecimal costo, Integer duracionMinutos, Integer capacidadMaxima,
                       Integer fkcodCategoria, String requisitos, String estado, String imageUrl) { // <-- Constructor actualizado
        this.codServicio = codServicio;
        this.nombreServicio = nombreServicio;
        this.descripcion = descripcion;
        this.costo = costo;
        this.duracionMinutos = duracionMinutos;
        this.capacidadMaxima = capacidadMaxima;
        this.fkcodCategoria = fkcodCategoria;
        this.requisitos = requisitos;
        this.estado = estado;
        this.imageUrl = imageUrl; // <-- Asignación
    }

    public Integer getCodServicio() {
        return codServicio;
    }

    public void setCodServicio(Integer codServicio) {
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

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public Integer getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(Integer capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public Integer getFkcodCategoria() {
        return fkcodCategoria;
    }

    public void setFkcodCategoria(Integer fkcodCategoria) {
        this.fkcodCategoria = fkcodCategoria;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getImageUrl() { // <-- Nuevo Getter
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) { // <-- Nuevo Setter
        this.imageUrl = imageUrl;
    }
}
