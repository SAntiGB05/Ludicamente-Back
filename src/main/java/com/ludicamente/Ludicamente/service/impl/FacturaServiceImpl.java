package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.model.Factura;
import com.ludicamente.Ludicamente.repository.FacturaRepository;
import com.ludicamente.Ludicamente.service.FacturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Override
    @Operation(summary = "Crear una nueva factura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Factura creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    public Factura crearFactura(Factura factura) {
        return facturaRepository.save(factura);
    }

    @Override
    @Operation(summary = "Obtener todas las facturas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de facturas obtenida exitosamente")
    })
    public List<Factura> listarFacturas() {
        return facturaRepository.findAll();
    }

    @Override
    @Operation(summary = "Actualizar una factura existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factura actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    public Optional<Factura> actualizarFactura(
            @Parameter(description = "ID de la factura a actualizar", example = "1") Integer id,
            Factura facturaActualizada) {

        Optional<Factura> facturaExistente = facturaRepository.findById(id);
        if (facturaExistente.isPresent()) {
            Factura factura = facturaExistente.get();
            factura.setFecha(facturaActualizada.getFecha());
            factura.setSubtotal(facturaActualizada.getSubtotal());
            factura.setImpuestos(facturaActualizada.getImpuestos());
            factura.setValorTotal(facturaActualizada.getValorTotal());
            factura.setEstado(facturaActualizada.getEstado());
            factura.setObservaciones(facturaActualizada.getObservaciones());
            factura.setNiño(facturaActualizada.getNiño());
            factura.setEmpleado(facturaActualizada.getEmpleado());
            return Optional.of(facturaRepository.save(factura));
        }
        return Optional.empty();
    }

    @Override
    @Operation(summary = "Eliminar una factura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Factura eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    public boolean eliminarFactura(@Parameter(description = "ID de la factura a eliminar", example = "1") Integer id) {
        if (facturaRepository.existsById(id)) {
            facturaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
