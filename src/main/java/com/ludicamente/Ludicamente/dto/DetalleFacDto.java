package com.ludicamente.Ludicamente.dto;

import java.math.BigDecimal;

public class DetalleFacDto {

    private Integer codDetalle;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotalItem;
    private Integer factura;
    private Integer servicio;

    public DetalleFacDto(Integer codDetalle, Integer cantidad, BigDecimal precioUnitario, BigDecimal subtotalItem, Integer factura, Integer servicio){
        this.codDetalle = codDetalle;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotalItem = subtotalItem;
        this.factura = factura;
        this.servicio = servicio;
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
        if (cantidad == null || cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        this.cantidad = cantidad;
    }


    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
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
}
