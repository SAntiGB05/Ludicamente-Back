package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.model.DetalleFactura;
import com.ludicamente.Ludicamente.service.DetalleFacService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detalle-facturas")
@PreAuthorize("hasAnyRole('ROL_ADMIN')")
public class DetalleFacController {

    @Autowired
    private DetalleFacService detalleFacturaService;

    @Operation(summary = "Crear un nuevo detalle de factura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Detalle de factura creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    @PostMapping
    public ResponseEntity<DetalleFactura> crearDetalleFactura(@RequestBody DetalleFactura detalleFactura) {
        DetalleFactura nuevo = detalleFacturaService.crearDetalle(detalleFactura);
        return ResponseEntity.status(201).body(nuevo);
    }

    @Operation(summary = "Listar todos los detalles de factura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<DetalleFactura>> listarDetalleFacturas() {
        return ResponseEntity.ok(detalleFacturaService.listarDetalles());
    }

    @Operation(summary = "Actualizar un detalle de factura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Detalle actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<DetalleFactura> actualizarDetalleFactura(
            @Parameter(description = "ID del detalle a actualizar", example = "1") @PathVariable Integer codDetalle,
            @RequestBody DetalleFactura detalleActualizado) {
        Optional<DetalleFactura> actualizado = detalleFacturaService.actualizarDetalle(codDetalle, detalleActualizado);
        return actualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un detalle de factura")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Detalle eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Detalle no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarDetalleFactura(
            @Parameter(description = "ID del detalle a eliminar", example = "1") @PathVariable Integer codDetalle) {
        if (detalleFacturaService.eliminarDetalle(codDetalle)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}