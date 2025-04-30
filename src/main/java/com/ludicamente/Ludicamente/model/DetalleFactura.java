package com.ludicamente.Ludicamente.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_factura")
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_detalle")
    private int codDetalle;

    @Column(name = "cantidad", nullable = false)
    private int cantidad = 1;

    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "descuento_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal descuentoUnitario = BigDecimal.ZERO;

    @Column(name = "subtotal_item", precision = 10, scale = 2, insertable = false, updatable = false)
    private BigDecimal subtotalItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FKid_factura", nullable = false)
    private Factura factura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FKcod_servicio", nullable = false)
    private Servicio servicio;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    // Constructor vacío
    public DetalleFactura() {}

    // Constructor con parámetros (opcional)
    public DetalleFactura(int cantidad, BigDecimal precioUnitario, BigDecimal descuentoUnitario, Factura factura, Servicio servicio) {
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.descuentoUnitario = descuentoUnitario;
        this.factura = factura;
        this.servicio = servicio;
    }

    // Getters y Setters
    public int getCodDetalle() {
        return codDetalle;
    }

    public void setCodDetalle(int codDetalle) {
        this.codDetalle = codDetalle;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
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

    public BigDecimal getSubtotalItem() {
        return subtotalItem;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}