package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.model.Empleado;
import com.ludicamente.Ludicamente.service.EmpleadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;


import java.util.List;
import java.util.Optional;

@PreAuthorize("hasAnyRole('ROL_STAFF','ROL_ADMIN')")
@RestController
@RequestMapping("/api/empleados")
@Tag(name = "Empleado", description = "API para gestión de empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping("/correo/{correo}")
    public ResponseEntity<Empleado> getEmpleadoByCorreo(@PathVariable String correo, Authentication authentication) {
        // Verificar que el correo solicitado coincide con el usuario autenticado
        if (!correo.equals(authentication.getName())) {
            return ResponseEntity.status(403).build(); // 403 Forbidden
        }

        try {
            Empleado empleado = empleadoService.obtenerEmpleadoPorCorreo(correo);
            return ResponseEntity.ok(empleado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build(); // 404 Not Found
        }
    }

    // Crear un empleado
    @Operation(summary = "Crear un nuevo empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empleado creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    @PostMapping
    public ResponseEntity<Empleado> crearEmpleado(@RequestBody Empleado empleado) {
        Empleado nuevoEmpleado = empleadoService.crearEmpleado(empleado);
        return ResponseEntity.status(201).body(nuevoEmpleado);
    }

    // Listar todos los empleados
    @Operation(summary = "Obtener todos los empleados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<Empleado>> listarEmpleados() {
        List<Empleado> empleados = empleadoService.listarEmpleados();
        return ResponseEntity.ok(empleados);
    }

    // Actualizar un empleado
    @Operation(summary = "Actualizar un empleado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(
            @Parameter(description = "ID del empleado a actualizar", example = "1")
            @PathVariable Integer id,
            @RequestBody Empleado empleadoActualizado) {

        Optional<Empleado> empleado = empleadoService.actualizarEmpleado(id, empleadoActualizado);
        return empleado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    // Eliminar un empleado
    @Operation(summary = "Eliminar un empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empleado eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(
            @Parameter(description = "ID del empleado a eliminar", example = "1")
            @PathVariable Integer id) {

        if (empleadoService.eliminarEmpleado(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}