package com.ludicamente.Ludicamente.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class FacturaDto {

    private Integer codFactura;
    private LocalDate fecha;
    private BigDecimal subtotal;
    private BigDecimal impuestos;
    private BigDecimal valorTotal;
    private Integer fkidNino;
    private Integer fkidEmpleado;

    private String estado;       // Enum como string
    private String metodoPago;

    // Constructor vacío
    public FacturaDto() {}

    // Constructor con parámetros

    public FacturaDto(Integer codFactura, LocalDate fecha, BigDecimal subtotal, BigDecimal impuestos, BigDecimal valorTotal, Integer fkidNino, Integer fkidEmpleado, String estado, String metodoPago) {
        this.codFactura = codFactura;
        this.fecha = fecha;
        this.subtotal = subtotal;
        this.impuestos = impuestos;
        this.valorTotal = valorTotal;
        this.fkidNino = fkidNino;
        this.fkidEmpleado = fkidEmpleado;
        this.estado = estado;
        this.metodoPago = metodoPago;
    }

    // Getters y Setters


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public Integer getCodFactura() {
        return codFactura;
    }

    public void setCodFactura(Integer codFactura) {
        this.codFactura = codFactura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
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

    public Integer getFkidNino() {
        return fkidNino;
    }

    public void setFkidNino(Integer fkidNino) {
        this.fkidNino = fkidNino;
    }

    public Integer getFkidEmpleado() {
        return fkidEmpleado;
    }

    public void setFkidEmpleado(Integer fkidEmpleado) {
        this.fkidEmpleado = fkidEmpleado;
    }
}
