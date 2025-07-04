package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.dto.DetalleFacDto;
import com.ludicamente.Ludicamente.model.DetalleFactura;

import java.util.List;
import java.util.Optional;

public interface DetalleFacService {
    DetalleFactura crearDetalle(DetalleFactura detalleFactura);
    List<DetalleFactura> listarDetalles();
    Optional<DetalleFactura> actualizarDetalle(Integer codDetalle, DetalleFactura detalleActualizado);
    boolean eliminarDetalle(Integer codDetalle);
    List<DetalleFactura> obtenerPorFactura(Integer codFactura);


}