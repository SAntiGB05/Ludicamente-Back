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
import com.ludicamente.Ludicamente.dto.EmpleadoDto; // Importación para la creación de empleados (POST)
import com.ludicamente.Ludicamente.dto.EmpleadoUpdateDto; // ¡NUEVA IMPORTACIÓN para la actualización de empleados (PUT)!

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Valid;

@PreAuthorize("hasAnyRole('ROL_STAFF','ROL_ADMIN')")
@RestController
@RequestMapping("/api/empleados")
@Tag(name = "Empleado", description = "API para gestión de empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Obtiene un empleado por su ID.
     * Mapea a /api/empleados/{id}
     *
     * @param id El ID del empleado a buscar.
     * @return ResponseEntity con el empleado si se encuentra, o 404 NOT FOUND si no.
     */
    @Operation(summary = "Obtener un empleado por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorId(@PathVariable Integer id) {
        Optional<Empleado> empleado = empleadoService.obtenerEmpleadoPorId(id);
        return empleado.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Obtener un empleado por su correo electrónico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado encontrado exitosamente"),
            @ApiResponse(responseCode = "403", description = "Acceso denegado"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Ajusta los roles según tu lógica de negocio
    @GetMapping("/correo/{correo}")
    public ResponseEntity<Empleado> getEmpleadoByCorreo(
            @PathVariable String correo,
            Authentication authentication
    ) {
        // Verificar si el correo coincide con el autenticado o si tiene rol de ADMIN
        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!correo.equals(authentication.getName()) && !esAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        try {
            Empleado empleado = empleadoService.obtenerEmpleadoPorCorreo(correo);
            return ResponseEntity.ok(empleado);
        } catch (RuntimeException e) {
            System.err.println("Error al obtener empleado por correo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }


    // Crear un empleado
    @Operation(summary = "Crear un nuevo empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empleado creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    @PreAuthorize("hasRole('ADMIN')") // Asegúrate de que este rol sea el correcto (ej. 'ROL_ADMIN')
    @PostMapping
    public ResponseEntity<Empleado> crearEmpleado(@Valid @RequestBody EmpleadoDto empleadoDto) {
        // Mapear EmpleadoDto a la entidad Empleado
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
        empleado.setEstado(Empleado.EstadoEmpleado.valueOf(empleadoDto.getEstado().toLowerCase()));

        // ¡IMPORTANTE! Hashear la contraseña antes de guardarla
        empleado.setContraseña(passwordEncoder.encode(empleadoDto.getContraseña()));

        Empleado nuevoEmpleado = empleadoService.crearEmpleado(empleado);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEmpleado);
    }


    @Operation(summary = "Obtener todos los empleados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida exitosamente")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')") // Ajusta los roles para listar empleados
    @GetMapping
    public ResponseEntity<List<Empleado>> listarEmpleados() {
        List<Empleado> empleados = empleadoService.listarEmpleados();
        return ResponseEntity.ok(empleados);
    }


    @Operation(summary = "Actualizar un empleado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(
            @Parameter(description = "ID del empleado a actualizar", example = "1")
            @PathVariable Integer id,
            // ¡CAMBIO CLAVE AQUÍ! Usamos EmpleadoUpdateDto para la actualización
            @Valid @RequestBody EmpleadoUpdateDto empleadoUpdateDto) {

        Optional<Empleado> existingEmpleadoOpt = empleadoService.obtenerEmpleadoPorId(id);

        if (!existingEmpleadoOpt.isPresent()) {
            return ResponseEntity.notFound().build(); // 404 Not Found si no existe
        }

        Empleado existingEmpleado = existingEmpleadoOpt.get();

        // Actualizar solo los campos que vienen en el EmpleadoUpdateDto
        // (La contraseña no está en EmpleadoUpdateDto, por lo tanto, no se intenta actualizar aquí)
        existingEmpleado.setCedulaEmpleado(empleadoUpdateDto.getCedulaEmpleado());
        existingEmpleado.setNombre(empleadoUpdateDto.getNombre());
        existingEmpleado.setCorreo(empleadoUpdateDto.getCorreo());
        existingEmpleado.setTelefono(empleadoUpdateDto.getTelefono());
        existingEmpleado.setDireccion(empleadoUpdateDto.getDireccion());
        existingEmpleado.setFechaContratacion(empleadoUpdateDto.getFechaContratacion());
        existingEmpleado.setSalario(empleadoUpdateDto.getSalario());
        existingEmpleado.setHorario(empleadoUpdateDto.getHorario());
        existingEmpleado.setNivelAcceso(empleadoUpdateDto.getNivelAcceso());
        // Asegúrate de que el String del DTO se convierta correctamente al Enum
        existingEmpleado.setEstado(Empleado.EstadoEmpleado.valueOf(empleadoUpdateDto.getEstado().toLowerCase()));

        Optional<Empleado> empleadoActualizado = empleadoService.actualizarEmpleado(id, existingEmpleado);
        return empleadoActualizado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    // Eliminar un empleado
    @Operation(summary = "Eliminar un empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empleado eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    @PreAuthorize("hasRole('ADMIN')") // Solo ADMIN puede eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(
            @Parameter(description = "ID del empleado a eliminar", example = "1")
            @PathVariable Integer id) {

        if (empleadoService.eliminarEmpleado(id)) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
    }
}