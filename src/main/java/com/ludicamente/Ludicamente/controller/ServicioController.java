package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.dto.ServicioDto;
import com.ludicamente.Ludicamente.mapper.ServicioMapper;
import com.ludicamente.Ludicamente.model.Categoria;
import com.ludicamente.Ludicamente.model.Servicio;
import com.ludicamente.Ludicamente.service.CategoriaService;
import com.ludicamente.Ludicamente.service.ServicioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    @Autowired
    private ServicioService servicioService;

    @Autowired
    private CategoriaService categoriaService;

    @PreAuthorize("hasAnyRole('ROL_ADMIN','ROL_STAFF')")
    @Operation(summary = "Crear un nuevo servicio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Servicio creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    @PostMapping
    public ResponseEntity<ServicioDto> crearServicio(@Valid @RequestBody ServicioDto dto) {
        Optional<Categoria> categoriaOpt = categoriaService.obtenerPorId(dto.getFkcodCategoria());
        if (categoriaOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        Servicio servicio = ServicioMapper.toEntity(dto, categoriaOpt.get());
        Servicio creado = servicioService.crearServicio(servicio);
        return ResponseEntity.created(URI.create("/api/servicios/" + creado.getCodServicio()))
                .body(ServicioMapper.toDto(creado));
    }

    @Operation(summary = "Obtener todos los servicios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de servicios obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<ServicioDto>> listarServicios() {
        List<ServicioDto> dtos = servicioService.listarServicios();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioDto> obtenerServicioPorId(@PathVariable Integer id) {
        Optional<Servicio> servicioOpt = servicioService.obtenerPorId(id);
        return servicioOpt
                .map(servicio -> ResponseEntity.ok(ServicioMapper.toDto(servicio)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAnyRole('ROL_ADMIN','ROL_STAFF')")
    @Operation(summary = "Actualizar un servicio existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Servicio actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Servicio o categoría no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ServicioDto> actualizarServicio(
            @PathVariable Integer id,
            @Valid @RequestBody ServicioDto dto) {
        Optional<ServicioDto> actualizado = servicioService.actualizarServicio(id, dto);
        return actualizado
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<ServicioDto>> listarPorCategoria(@PathVariable Integer idCategoria) {
        List<ServicioDto> servicios = servicioService.listarServicios()
                .stream()
                .filter(dto -> idCategoria.equals(dto.getFkcodCategoria()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(servicios);
    }

    @PreAuthorize("hasAnyRole('ROL_ADMIN','ROL_STAFF')")
    @Operation(summary = "Eliminar un servicio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Servicio eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Servicio no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarServicio(
            @Parameter(description = "ID del servicio a eliminar", example = "1")
            @PathVariable Integer id) {

        boolean eliminado = servicioService.eliminarServicio(id);
        return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
