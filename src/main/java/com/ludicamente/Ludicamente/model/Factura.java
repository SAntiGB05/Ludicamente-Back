package com.ludicamente.Ludicamente.model;

import com.fasterxml.jackson.annotation.JsonBackReference; // Importar esta anotación
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "factura")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_factura")
    private Integer codFactura;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fecha;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal descuento;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal impuestos;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoFactura estado;

    @Column
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "fkid_niño", nullable = false)
    // Asumiendo que Niño tiene una relación OneToMany con Factura,
    // y Niño tendrá @JsonManagedReference("niño-facturas")
    @JsonBackReference("niño-facturas") // Añadida esta línea
    private Niño niño;

    @ManyToOne
    @JoinColumn(name = "fkid_empleado", nullable = true)
    @JsonBackReference("empleado-facturas") // Añadida esta línea
    private Empleado empleado;

    // Enums internos para método de pago y estado de la factura
    public enum MetodoPago {
        EFECTIVO, TARJETA, TRANSFERENCIA
    }

    public enum EstadoFactura {
        PENDIENTE, PAGADA, CANCELADA
    }

    // Getters y setters

    public Integer getCodFactura() {
        return codFactura;
    }

    public void setCodFactura(Integer codFactura) {
        this.codFactura = codFactura;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Niño getNiño() {
        return niño;
    }

    public void setNiño(Niño niño) {
        this.niño = niño;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
}