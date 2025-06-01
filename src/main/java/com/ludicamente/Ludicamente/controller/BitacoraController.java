package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.model.Bitacora;
import com.ludicamente.Ludicamente.service.BitacoraService;
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
@RequestMapping("/api/bitacoras")
public class BitacoraController {

    @Autowired
    private BitacoraService bitacoraService;

    @PreAuthorize("hasAnyRole('ROL_ADMIN','RON_ACUDIENTE')")
    @Operation(summary = "Crear una nueva bitácora")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bitácora creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    @PostMapping
    public ResponseEntity<Bitacora> crearBitacora(@RequestBody Bitacora bitacora) {
        Bitacora nuevaBitacora = bitacoraService.crearBitacora(bitacora);
        return ResponseEntity.status(201).body(nuevaBitacora);
    }

    @PreAuthorize("hasAnyRole('ROL_ADMIN')")
    @Operation(summary = "Obtener todas las bitácoras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de bitácoras obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<Bitacora>> listarBitacoras() {
        List<Bitacora> bitacoras = bitacoraService.listarBitacoras();
        return ResponseEntity.ok(bitacoras);
    }

    @PreAuthorize("hasAnyRole('ROL_ACUDIENTE','ROL_ADMIN')")
    @Operation(summary = "Obtener bitácoras activas de un niño")
    @GetMapping("/niño/{idNiño}")
    public ResponseEntity<List<Bitacora>> listarBitacorasPorNiño(
            @PathVariable Integer idNiño) {
        List<Bitacora> bitacoras = bitacoraService.findByNiñoAndEstadoTrue(idNiño);
        return ResponseEntity.ok(bitacoras);
    }

    @PreAuthorize("hasAnyRole('ROL_ADMIN')")

    @Operation(summary = "Actualizar una bitácora existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bitácora actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bitácora no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Bitacora> actualizarBitacora(
            @Parameter(description = "ID de la bitácora a actualizar", example = "1")
            @PathVariable Integer id,
            @RequestBody Bitacora bitacoraActualizada) {

        Optional<Bitacora> bitacora = bitacoraService.actualizarBitacora(id, bitacoraActualizada);
        return bitacora.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ROL_ADMIN')")
    @Operation(summary = "Eliminar una bitácora")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bitácora eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bitácora no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarBitacora(
            @Parameter(description = "ID de la bitácora a eliminar", example = "1")
            @PathVariable Integer id) {

        if (bitacoraService.eliminarBitacora(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}