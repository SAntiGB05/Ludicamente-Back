package com.ludicamente.Ludicamente.dto;

import java.util.List;

/**
 * DTO que representa una factura junto con sus detalles.
 */
public class FacturaConDetallesDto {

    private FacturaDto factura;
    private List<DetalleFacDto> detalles;

    // Constructor vacío
    public FacturaConDetallesDto() {}

    // Constructor completo
    public FacturaConDetallesDto(FacturaDto factura, List<DetalleFacDto> detalles) {
        this.factura = factura;
        this.detalles = detalles;
    }

    // Getters y setters
    public FacturaDto getFactura() {
        return factura;
    }

    public void setFactura(FacturaDto factura) {
        this.factura = factura;
    }

    public List<DetalleFacDto> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleFacDto> detalles) {
        this.detalles = detalles;
    }
}
