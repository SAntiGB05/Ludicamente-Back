package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.model.DetalleFactura;
import com.ludicamente.Ludicamente.repository.DetalleFacRepository;
import com.ludicamente.Ludicamente.service.DetalleFacService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleFacServiceImpl implements DetalleFacService {

    @Autowired
    private DetalleFacRepository detalleFacRepository;

    @Override
    @Operation(summary = "Crear un nuevo detalle de factura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Detalle de factura creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos para el detalle de factura")
    })
    public DetalleFactura crearDetalle(DetalleFactura detalleFactura) {
        return detalleFacRepository.save(detalleFactura);
    }

    @Override
    @Operation(summary = "Listar todos los detalles de factura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles de factura listados exitosamente")
    })
    public List<DetalleFactura> listarDetalles() {
        return detalleFacRepository.findAll();
    }

    @Override
    @Operation(summary = "Actualizar un detalle de factura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    })
    public Optional<DetalleFactura> actualizarDetalle(
            @Parameter(description = "ID del detalle a actualizar", example = "1") Integer codDetalle,
            DetalleFactura detalleActualizado) {

        Optional<DetalleFactura> existente = detalleFacRepository.findById(codDetalle);
        if (existente.isPresent()) {
            DetalleFactura detalle = existente.get();
            detalle.setCantidad(detalleActualizado.getCantidad());
            detalle.setPrecioUnitario(detalleActualizado.getPrecioUnitario());
            detalle.setDescuentoUnitario(detalleActualizado.getDescuentoUnitario());
            detalle.setObservaciones(detalleActualizado.getObservaciones());
            detalle.setFactura(detalleActualizado.getFactura());
            detalle.setServicio(detalleActualizado.getServicio());
            return Optional.of(detalleFacRepository.save(detalle));
        }
        return Optional.empty();
    }

    @Override
    @Operation(summary = "Eliminar un detalle de factura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Detalle eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    })
    public boolean eliminarDetalle(@Parameter(description = "ID del detalle a eliminar", example = "1") Integer codDetalle) {
        if (detalleFacRepository.existsById(codDetalle)) {
            detalleFacRepository.deleteById(codDetalle);
            return true;
        }
        return false;
    }


    @Override
    public List<DetalleFactura> obtenerPorFactura(Integer codFactura) {
        System.out.println("Buscando detalles de la factura: " + codFactura);
        return detalleFacRepository.findByFactura_CodFactura(codFactura);
    }


}