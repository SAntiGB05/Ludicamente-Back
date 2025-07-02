package com.ludicamente.Ludicamente.dto;

import java.math.BigDecimal;

public class DetalleFacDto {

    private Integer codDetalle;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal descuentoUnitario;
    private String hora;
    private String fecha;
    private BigDecimal subtotalItem;
    private Integer factura; // This might be used as fkidNino or a temporary order ID
    private Integer servicio;
    private String observaciones;
    private String descripcion;
    private String nombreCliente;
    private String telefonoCliente;
    private String emailCliente;

    public DetalleFacDto(Integer codDetalle, Integer cantidad, BigDecimal precioUnitario,
                         BigDecimal descuentoUnitario, String hora, String fecha,
                         BigDecimal subtotalItem, Integer factura, Integer servicio,
                         String observaciones, String descripcion, String nombreCliente,
                         String telefonoCliente, String emailCliente) {
        this.codDetalle = codDetalle;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuentoUnitario = descuentoUnitario;
        this.hora = hora;
        this.fecha = fecha;
        this.subtotalItem = subtotalItem;
        this.factura = factura;
        this.servicio = servicio;
        this.observaciones = observaciones;
        this.descripcion = descripcion;
        this.nombreCliente = nombreCliente;
        this.telefonoCliente = telefonoCliente;
        this.emailCliente = emailCliente;
    }

    // Default constructor added for convenience if needed for deserialization
    public DetalleFacDto() {
    }

    public Integer getCodDetalle() {
        return codDetalle;
    }

    public void setCodDetalle(Integer codDetalle) {
        this.codDetalle = codDetalle;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public BigDecimal getDescuentoUnitario() {
        return descuentoUnitario;
    }

    public void setDescuentoUnitario(BigDecimal descuentoUnitario) {
        this.descuentoUnitario = descuentoUnitario;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getSubtotalItem() {
        return subtotalItem;
    }

    public void setSubtotalItem(BigDecimal subtotalItem) {
        this.subtotalItem = subtotalItem;
    }

    public Integer getFactura() {
        return factura;
    }

    public void setFactura(Integer factura) {
        this.factura = factura;
    }

    public Integer getServicio() {
        return servicio;
    }

    public void setServicio(Integer servicio) {
        this.servicio = servicio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }
}
