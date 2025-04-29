package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.model.Bitacora;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Optional;

public interface BitacoraService {

    @Operation(summary = "Crear una nueva bitácora")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bitácora creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    Bitacora crearBitacora(Bitacora bitacora);

    @Operation(summary = "Obtener todas las bitácoras")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de bitácoras obtenida exitosamente")
    })
    List<Bitacora> listarBitacoras();

    @Operation(summary = "Actualizar una bitácora existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bitácora actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bitácora no encontrada")
    })
    Optional<Bitacora> actualizarBitacora(
            @Parameter(description = "ID de la bitácora a actualizar", example = "1") Integer id,
            Bitacora bitacoraActualizada);

    @Operation(summary = "Eliminar una bitácora")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Bitácora eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Bitácora no encontrada")
    })
    boolean eliminarBitacora(@Parameter(description = "ID de la bitácora a eliminar", example = "1") Integer id);
}