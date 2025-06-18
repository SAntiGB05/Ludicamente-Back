package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.model.Empleado;
import com.ludicamente.Ludicamente.service.EmpleadoService;
import com.ludicamente.Ludicamente.dto.EmpleadoDto;
import com.ludicamente.Ludicamente.dto.EmpleadoUpdateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@PreAuthorize("hasAnyRole('ROL_STAFF','ROL_ADMIN')")
@RestController
@RequestMapping("/api/empleados")
@Tag(name = "Empleado", description = "API para gestión de empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Obtener un empleado por su correo electrónico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado encontrado exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    @PreAuthorize("hasAnyRole('ROL_ADMIN','ROL_STAFF')")
    @GetMapping("/correo/{correo}")
    public ResponseEntity<Empleado> getEmpleadoByCorreo(@PathVariable String correo, Authentication authentication) {
        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!correo.equals(authentication.getName()) && !esAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
        }

        try {
            Empleado empleado = empleadoService.obtenerEmpleadoPorCorreo(correo);
            return ResponseEntity.ok(empleado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404
        }
    }

    @Operation(summary = "Crear un nuevo empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empleado creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    @PreAuthorize("hasRole('ROL_ADMIN')")
    @PostMapping
    public ResponseEntity<Empleado> crearEmpleado(@Valid @RequestBody EmpleadoDto empleadoDto) {
        Empleado empleado = new Empleado();
        empleado.setCedulaEmpleado(empleadoDto.getCedulaEmpleado());
        empleado.setNombre(empleadoDto.getNombre());
        empleado.setCorreo(empleadoDto.getCorreo());
        empleado.setTelefono(empleadoDto.getTelefono());
        empleado.setDireccion(empleadoDto.getDireccion());
        empleado.setFechaContratacion(empleadoDto.getFechaContratacion());
        empleado.setSalario(empleadoDto.getSalario());
        empleado.setHorario(empleadoDto.getHorario());
        empleado.setNivelAcceso(empleadoDto.getNivelAcceso());
        empleado.setEstado(Empleado.EstadoEmpleado.valueOf(empleadoDto.getEstado().toUpperCase()));

        // Hash de contraseña
        empleado.setContraseña(passwordEncoder.encode(empleadoDto.getContraseña()));

        Empleado nuevoEmpleado = empleadoService.crearEmpleado(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEmpleado);
    }

    @Operation(summary = "Obtener todos los empleados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida exitosamente")
    })
    @PreAuthorize("hasAnyRole('ROL_ADMIN','ROL_STAFF')")
    @GetMapping
    public ResponseEntity<List<Empleado>> listarEmpleados() {
        List<Empleado> empleados = empleadoService.listarEmpleados();
        return ResponseEntity.ok(empleados);
    }

    @Operation(summary = "Actualizar un empleado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    @PreAuthorize("hasRole('ROL_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(
            @PathVariable Integer id,
            @Valid @RequestBody EmpleadoUpdateDto empleadoUpdateDto) {

        Optional<Empleado> empleadoOpt = empleadoService.obtenerEmpleadoPorId(id);
        if (empleadoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Empleado empleado = empleadoOpt.get();
        empleado.setCedulaEmpleado(empleadoUpdateDto.getCedulaEmpleado());
        empleado.setNombre(empleadoUpdateDto.getNombre());
        empleado.setCorreo(empleadoUpdateDto.getCorreo());
        empleado.setTelefono(empleadoUpdateDto.getTelefono());
        empleado.setDireccion(empleadoUpdateDto.getDireccion());
        empleado.setFechaContratacion(empleadoUpdateDto.getFechaContratacion());
        empleado.setSalario(empleadoUpdateDto.getSalario());
        empleado.setHorario(empleadoUpdateDto.getHorario());
        empleado.setNivelAcceso(empleadoUpdateDto.getNivelAcceso());
        empleado.setEstado(Empleado.EstadoEmpleado.valueOf(empleadoUpdateDto.getEstado().toUpperCase()));

        Optional<Empleado> actualizado = empleadoService.actualizarEmpleado(id, empleado);
        return actualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empleado eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    @PreAuthorize("hasRole('ROL_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Integer id) {
        if (empleadoService.eliminarEmpleado(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
