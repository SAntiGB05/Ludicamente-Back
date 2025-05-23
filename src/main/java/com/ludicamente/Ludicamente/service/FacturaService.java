package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.model.Factura;

import java.util.List;
import java.util.Optional;

public interface FacturaService {

    Factura crearFactura(Factura factura);

    List<Factura> listarFacturas();

    Optional<Factura> actualizarFactura(Integer id, Factura facturaActualizada);

    boolean eliminarFactura(Integer id);
}
