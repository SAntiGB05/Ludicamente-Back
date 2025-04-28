package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.model.Niño;
import com.ludicamente.Ludicamente.service.NiñoService;
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
@RequestMapping("/api/niños")
public class NiñoController {

    @Autowired
    private NiñoService niñoService;

    // Crear un niño
    @Operation(summary = "Crear un nuevo niño")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Niño creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    @PostMapping
    public ResponseEntity<Niño> crearNiño(@RequestBody Niño niño) {
        Niño nuevoNiño = niñoService.crearNiño(niño);
        return ResponseEntity.status(201).body(nuevoNiño);
    }

    // Listar todos los niños
    @Operation(summary = "Obtener todos los niños")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de niños obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<Niño>> listarNiños() {
        List<Niño> niños = niñoService.listarNiños();
        return ResponseEntity.ok(niños);
    }

    // Actualizar un niño
    @Operation(summary = "Actualizar un niño existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Niño actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Niño no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Niño> actualizarNiño(
            @Parameter(description = "ID del niño a actualizar", example = "1")
            @PathVariable Integer id,
            @RequestBody Niño niñoActualizado) {

        Optional<Niño> niño = niñoService.actualizarNiño(id, niñoActualizado);
        return niño.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Eliminar un niño
    @Operation(summary = "Eliminar un niño")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Niño eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Niño no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNiño(
            @Parameter(description = "ID del niño a eliminar", example = "1")
            @PathVariable Integer id) {

        if (niñoService.eliminarNiño(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}