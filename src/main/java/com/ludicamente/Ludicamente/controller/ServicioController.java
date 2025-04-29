package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.model.Servicio;
import com.ludicamente.Ludicamente.service.ServicioService;
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
@RequestMapping("/api/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @Operation(summary = "Crear un nuevo servicio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Servicio creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    @PostMapping
    public ResponseEntity<Servicio> crearServicio(@RequestBody Servicio servicio) {
        Servicio nuevoServicio = servicioService.crearServicio(servicio);
        return ResponseEntity.status(201).body(nuevoServicio);
    }

    @Operation(summary = "Obtener todos los servicios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de servicios obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<Servicio>> listarServicios() {
        List<Servicio> servicios = servicioService.listarServicios();
        return ResponseEntity.ok(servicios);
    }

    @Operation(summary = "Actualizar un servicio existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Servicio actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Servicio> actualizarServicio(
            @Parameter(description = "ID del servicio a actualizar", example = "1")
            @PathVariable Integer id,
            @RequestBody Servicio servicioActualizado) {

        Optional<Servicio> servicio = servicioService.actualizarServicio(id, servicioActualizado);
        return servicio.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un servicio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Servicio eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarServicio(
            @Parameter(description = "ID del servicio a eliminar", example = "1")
            @PathVariable Integer id) {

        if (servicioService.eliminarServicio(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}