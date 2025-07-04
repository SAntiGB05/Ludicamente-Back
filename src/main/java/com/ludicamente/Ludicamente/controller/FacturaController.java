package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.dto.FacturaConDetallesDto;
import com.ludicamente.Ludicamente.dto.FacturaDto;
import com.ludicamente.Ludicamente.model.DetalleFactura;
import com.ludicamente.Ludicamente.model.Factura;
import com.ludicamente.Ludicamente.service.DetalleFacService;
import com.ludicamente.Ludicamente.service.FacturaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private DetalleFacService detalleFacService;

    // Crear una factura
    @Operation(summary = "Crear una nueva factura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Factura creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    @PostMapping
    public ResponseEntity<Factura> crearFactura(@RequestBody Factura factura) {
        Factura nuevaFactura = facturaService.crearFactura(factura);
        return ResponseEntity.status(201).body(nuevaFactura);
    }

    // Listar todas las facturas
    @Operation(summary = "Obtener todas las facturas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de facturas obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<Factura>> listarFacturas() {
        List<Factura> facturas = facturaService.listarFacturas();
        return ResponseEntity.ok(facturas);
    }

    // Listar facturas como DTO
    @GetMapping("/dto")
    @Operation(summary = "Listar facturas como DTO")
    public ResponseEntity<List<FacturaDto>> listarFacturasDto() {
        List<FacturaDto> facturasDto = facturaService.obtenerFacturasDto();
        return ResponseEntity.ok(facturasDto);
    }

    // Actualizar una factura
    @Operation(summary = "Actualizar una factura existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factura actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Factura> actualizarFactura(
            @Parameter(description = "ID de la factura a actualizar", example = "1")
            @PathVariable Integer id,
            @RequestBody Factura facturaActualizada) {

        Optional<Factura> factura = facturaService.actualizarFactura(id, facturaActualizada);
        return factura.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Ver factura con detalles como DTO combinado
    @Operation(summary = "Obtener una factura con sus detalles (DTO combinado)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Factura con detalles obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @GetMapping("/{id}/detalle")
    public ResponseEntity<FacturaConDetallesDto> verFacturaConDetalles(@PathVariable Integer id) {
        Optional<FacturaConDetallesDto> dto = facturaService.obtenerFacturaConDetallesDto(id);
        return dto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Ver lista de DetalleFactura por ID de factura
    @Operation(summary = "Listar detalles de una factura por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalles encontrados"),
            @ApiResponse(responseCode = "404", description = "No se encontraron detalles para la factura")
    })
    @GetMapping("/{id}/items")
    public ResponseEntity<List<DetalleFactura>> obtenerItemsPorFactura(@PathVariable Integer id) {
        List<DetalleFactura> detalles = detalleFacService.obtenerPorFactura(id);
        if (detalles.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(detalles);
    }

    // Eliminar una factura
    @Operation(summary = "Eliminar una factura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Factura eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Factura no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFactura(
            @Parameter(description = "ID de la factura a eliminar", example = "1")
            @PathVariable Integer id) {

        if (facturaService.eliminarFactura(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}
