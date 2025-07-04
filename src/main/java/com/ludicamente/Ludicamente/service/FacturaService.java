package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.dto.DetalleFacDto;
import com.ludicamente.Ludicamente.dto.FacturaConDetallesDto;
import com.ludicamente.Ludicamente.dto.FacturaDto;
import com.ludicamente.Ludicamente.model.Factura;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para gestionar operaciones relacionadas con facturas.
 */
public interface FacturaService {

    Factura crearFactura(Factura factura);
    List<Factura> listarFacturas();
    List<FacturaDto> obtenerFacturasDto();
    Optional<FacturaConDetallesDto> obtenerFacturaConDetallesDto(Integer codFactura);
    Optional<Factura> actualizarFactura(Integer id, Factura facturaActualizada);
    boolean eliminarFactura(Integer id);
    FacturaDto crearFacturaConDetalles(FacturaConDetallesDto dto);

}
