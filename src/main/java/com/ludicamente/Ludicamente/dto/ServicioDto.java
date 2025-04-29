package com.ludicamente.Ludicamente.dto;

import java.math.BigDecimal;

public class ServicioDto {

    private Integer codServicio;
    private String nombreServicio;
    private String descripcion;
    private BigDecimal costo;
    private Integer fkcodCategoria;

    // Constructor vacío
    public ServicioDto() {}

    // Constructor con parámetros
    public ServicioDto(Integer codServicio, String nombreServicio, String descripcion,
                       BigDecimal costo, Integer fkcodCategoria) {
        this.codServicio = codServicio;
        this.nombreServicio = nombreServicio;
        this.descripcion = descripcion;
        this.costo = costo;
        this.fkcodCategoria = fkcodCategoria;
    }

    // Getters y Setters
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

    public Integer getFkcodCategoria() {
        return fkcodCategoria;
    }

    public void setFkcodCategoria(Integer fkcodCategoria) {
        this.fkcodCategoria = fkcodCategoria;
    }
}
