package com.ludicamente.Ludicamente.dto;

import java.math.BigDecimal;

public class DetallePagoDTO {

    private String descripcion;
    private Integer cantidad;
    private BigDecimal precio;

    private Integer idNino;
    private Integer idEmpleado;
    private Integer idServicio;

    public DetallePagoDTO() {}

    public DetallePagoDTO(String descripcion, Integer cantidad, BigDecimal precio,
                          Integer idNino, Integer idEmpleado, Integer idServicio) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precio = precio;
        this.idNino = idNino;
        this.idEmpleado = idEmpleado;
        this.idServicio = idServicio;
    }

    // Getters y Setters

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getIdNino() {
        return idNino;
    }

    public void setIdNino(Integer idNino) {
        this.idNino = idNino;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Integer getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Integer idServicio) {
        this.idServicio = idServicio;
    }
}
