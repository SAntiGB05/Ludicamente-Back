package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.auth.AuthenticationService;
import com.ludicamente.Ludicamente.dto.NiñoDto;
import com.ludicamente.Ludicamente.model.Niño;
import com.ludicamente.Ludicamente.service.NiñoService;
import com.ludicamente.Ludicamente.dto.NiñoDto; // <--- Importa el NiñoDto
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
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
    public ResponseEntity<NiñoDto> crearNiño(@RequestBody NiñoDto niñoDto) {
        NiñoDto nuevoNiño = niñoService.crearNiño(niñoDto);
        return ResponseEntity.status(201).body(nuevoNiño);
    }

    // Listar todos los niños
    @Operation(summary = "Obtener todos los niños")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de niños obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<NiñoDto>> listarNiños(Authentication authentication) {
        String correo = authentication.getName();

        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
        roles.forEach(r -> System.out.println("ROL DETECTADO: " + r.getAuthority()));

        boolean esAdmin = roles.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean esEmpleado = roles.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_STAFF"));

        if (esAdmin || esEmpleado) {
            System.out.println(">>> LISTANDO TODOS LOS NIÑOS (admin/empleado)");
            return ResponseEntity.ok(niñoService.listarTodosLosNiños());
        }

        return ResponseEntity.ok(niñoService.listarNiñosPorCorreoAcudiente(correo));
    }

    @Operation(summary = "Obtener un niño por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Niño encontrado"),
            @ApiResponse(responseCode = "404", description = "Niño no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NiñoDto> obtenerNiñoPorId(@PathVariable Integer id) {
        Optional<NiñoDto> niño = niñoService.obtenerNiñoPorId(id);
        return niño.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    @GetMapping(params = "acudiente")
    public ResponseEntity<List<NiñoDto>> listarNiñosPorAcudiente(@RequestParam("acudiente") Integer idAcudiente) {
        List<NiñoDto> niños = niñoService.listarNiñosPorAcudiente(idAcudiente);
        return ResponseEntity.ok(niños);
    }

    // Actualizar un niño
    @Operation(summary = "Actualizar un niño existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Niño actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Niño no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<NiñoDto> actualizarNiño(
            @Parameter(description = "ID del niño a actualizar", example = "1")
            @PathVariable Integer id,
            @RequestBody NiñoDto niñoActualizado // Se recibe como DTO
    ) {
        Optional<NiñoDto> actualizado = niñoService.actualizarNiño(id, niñoActualizado);

        return actualizado
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    @PutMapping("/{id}/foto")
    public ResponseEntity<Void> actualizarFotoNiño(
            @PathVariable Integer id,
            @RequestParam("foto") MultipartFile foto) {
        niñoService.actualizarFoto(id, foto);
        return ResponseEntity.ok().build();
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
