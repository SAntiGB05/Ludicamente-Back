package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.dto.BitacoraDto;
import com.ludicamente.Ludicamente.mapper.BitacoraMapper;
import com.ludicamente.Ludicamente.model.Bitacora;
import com.ludicamente.Ludicamente.service.BitacoraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bitacoras")
public class BitacoraController {

    @Autowired
    private BitacoraService bitacoraService;

    @PreAuthorize("hasAnyRole('ROL_ADMIN','ROL_STAFF')")
    @Operation(summary = "Crear una nueva bitácora")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bitácora creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    @PostMapping
    public ResponseEntity<BitacoraDto> crearBitacora(@Valid @RequestBody BitacoraDto dto) {
        Bitacora nueva = bitacoraService.crearBitacoraDesdeDto(dto);
        return ResponseEntity.created(URI.create("/api/bitacoras/" + nueva.getCodBitacora()))
                .body(BitacoraMapper.toDto(nueva));
    }

    @GetMapping("/historial/{idNiño}")
    public ResponseEntity<List<BitacoraDto>> obtenerHistorial(@PathVariable Integer idNiño) {
        List<BitacoraDto> historial = bitacoraService.obtenerHistorialPorNiño(idNiño);
        return ResponseEntity.ok(historial);
    }


    @PreAuthorize("hasAnyRole('ROL_ACUDIENTE','ROL_ADMIN')")
    @Operation(summary = "Obtener bitácoras activas de un niño")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bitácoras activas del niño obtenidas exitosamente"),
            @ApiResponse(responseCode = "404", description = "Niño no encontrado")
    })
    @GetMapping("/niño/{idNiño}")
    public ResponseEntity<List<BitacoraDto>> listarBitacorasPorNiño(
            @Parameter(description = "ID del niño", example = "1")
            @PathVariable("idNiño") Integer idNiño) {

        List<Bitacora> bitacoras = bitacoraService.findByNiñoAndEstadoTrue(idNiño);
        List<BitacoraDto> dtos = bitacoras.stream()
                .map(BitacoraMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('ROL_ADMIN')")
    @Operation(summary = "Obtener todas las bitácoras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de bitácoras obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<BitacoraDto>> listarBitacoras() {
        List<Bitacora> bitacoras = bitacoraService.listarBitacoras();
        List<BitacoraDto> dtos = bitacoras.stream()
                .map(BitacoraMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @PreAuthorize("hasRole('ROL_ADMIN')")
    @Operation(summary = "Actualizar una bitácora existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bitácora actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bitácora no encontrada")
    })
    @PutMapping(value = "/{idBitacora}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BitacoraDto> actualizarBitacora(
            @Parameter(description = "ID de la bitácora a actualizar", example = "1")
            @PathVariable Integer idBitacora,
            @Valid @RequestBody BitacoraDto dto) {

        Optional<Bitacora> actualizada = bitacoraService.actualizarBitacora(idBitacora, BitacoraMapper.toEntity(dto));
        return actualizada.map(bit -> ResponseEntity.ok(BitacoraMapper.toDto(bit)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PreAuthorize("hasRole('ROL_ADMIN','ROL_STAFF')")
    @Operation(summary = "Archivar una bitácora (marcar como inactiva)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bitácora archivada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bitácora no encontrada")
    })
    @PutMapping("/{idBitacora}/archivar")
    public ResponseEntity<BitacoraDto> archivarBitacora(
            @Parameter(description = "ID de la bitácora a archivar", example = "1")
            @PathVariable Integer idBitacora) {

        Optional<Bitacora> archivada = bitacoraService.archivarBitacora(idBitacora);
        return archivada
                .map(bit -> ResponseEntity.ok(BitacoraMapper.toDto(bit)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ROL_ADMIN','ROL_STAFF','ROL_ACUDIENTE')")
    @Operation(summary = "Obtener una bitácora específica de un niño")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bitácora encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bitácora o niño no encontrado")
    })
    @GetMapping("/niño/{idNiño}/bitacora/{codBitacora}")
    public ResponseEntity<BitacoraDto> obtenerBitacoraPorNiñoYCodigo(
            @PathVariable Integer idNiño,
            @PathVariable Integer codBitacora) {

        Optional<Bitacora> bitacoraOpt = bitacoraService.findByNiñoAndCodBitacora(idNiño, codBitacora);
        return bitacoraOpt
                .map(bitacora -> ResponseEntity.ok(BitacoraMapper.toDto(bitacora)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}
