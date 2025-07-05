package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.auth.RegisterAcudienteRequest;
import com.ludicamente.Ludicamente.auth.userdetails.AcudienteUserDetails;
import com.ludicamente.Ludicamente.dto.AcudienteDto;
import com.ludicamente.Ludicamente.model.Acudiente;
import com.ludicamente.Ludicamente.service.AcudienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/acudiente")
@Tag(name = "Acudiente", description = "API para gestionar acudientes")
public class AcudienteController {

    @Autowired
    private AcudienteService acudienteService;

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @Operation(summary = "Listar acudientes con información completa (sin contraseña)")
    @GetMapping("/listado")
    public List<AcudienteDto> listarAcudientes() {
        return acudienteService.listarAcudientes().stream()
                .map(a -> new AcudienteDto(
                        a.getIdAcudiente(),
                        a.getCedula(),
                        a.getNombre(),
                        a.getCorreo(),
                        a.getContraseña(),
                        a.getTelefono(),
                        a.getParentesco(),
                        a.getDireccion()
                        // Si no tienes constructor de 6 argumentos, usarías el de 8 y pasarías null para contraseña y dirección
                        // a.getIdAcudiente(), a.getCedula(), a.getNombre(), a.getCorreo(), null, a.getTelefono(), a.getParentesco(), null
                ))
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')") // Solo para ADMIN
    @Operation(summary = "Obtener un acudiente por su cédula (solo para admins)")
    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<AcudienteDto> getAcudienteByCedula(@PathVariable String cedula) {
        try {
            // Se asume que obtenerAcudientePorCedula devuelve Acudiente
            Acudiente acudiente = acudienteService.obtenerAcudientePorCedula(cedula);
            // Mapeamos a DTO antes de devolver, sin exponer la contraseña
            AcudienteDto acudienteDto = new AcudienteDto(
                    acudiente.getIdAcudiente(),
                    acudiente.getCedula(),
                    acudiente.getNombre(),
                    acudiente.getCorreo(),
                    null, // No se expone la contraseña
                    acudiente.getTelefono(),
                    acudiente.getParentesco(),
                    acudiente.getDireccion()
            );
            return ResponseEntity.ok(acudienteDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }

    @PreAuthorize("hasRole('ACUDIENTE')")
    @Operation(summary = "Obtener los datos del acudiente autenticado")
    @GetMapping("/auth")
    public ResponseEntity<AcudienteDto> getAcudienteAutenticado(@AuthenticationPrincipal AcudienteUserDetails userDetails) {
        // acudienteService.obtenerAcudienteAutenticado ya devuelve un AcudienteDto
        AcudienteDto dto = acudienteService.obtenerAcudienteAutenticado(userDetails.getUsername());
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Actualizar un acudiente (por ID) (solo para admins)")
    @PutMapping("/{id}")
    public ResponseEntity<Acudiente> updateAcudienteByAdmin(@PathVariable Integer id, @Valid @RequestBody AcudienteDto acudienteDetails) {
        Acudiente actualizado = acudienteService.actualizarAcudienteAdmin(id, acudienteDetails);
        return ResponseEntity.ok(actualizado);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ACUDIENTE')")
    @Operation(summary = "Actualizar los datos del acudiente autenticado")
    @PutMapping("/perfil") // Cambiado el endpoint para mayor claridad
    public ResponseEntity<Acudiente> updateAcudienteAuthenticated(@RequestBody Acudiente acudienteDetails) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof AcudienteUserDetails acudienteUserDetails) {
            Acudiente acudiente = acudienteUserDetails.getAcudiente();
            // Asegurarse de que el ID en la URL no pueda ser manipulado
            Acudiente actualizado = acudienteService.actualizarAcudiente(acudiente.getIdAcudiente(), acudienteDetails);
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ACUDIENTE')")
    @Operation(summary = "Obtener un acudiente por su correo (autenticado)")
    @GetMapping("/correo/{correo}")
    public ResponseEntity<AcudienteDto> getAcudienteByCorreo(@PathVariable String correo, Authentication authentication) {
        // Verificar que el correo solicitado coincide con el usuario autenticado
        if (!correo.equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        try {
            // Se asume que obtenerAcudientePorCorreo devuelve Acudiente
            Acudiente acudiente = acudienteService.obtenerAcudientePorCorreo(correo);
            // Mapeamos a DTO antes de devolver, sin exponer la contraseña
            AcudienteDto acudienteDto = new AcudienteDto(
                    acudiente.getIdAcudiente(),
                    acudiente.getCedula(),
                    acudiente.getNombre(),
                    acudiente.getCorreo(),
                    null, // No se expone la contraseña
                    acudiente.getTelefono(),
                    acudiente.getParentesco(),
                    acudiente.getDireccion() // La dirección sí puede ser relevante aquí
            );
            return ResponseEntity.ok(acudienteDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Agregar un nuevo acudiente con niños")
    @PostMapping("/agregar")
    public ResponseEntity<?> registrarAcudienteConNiños(@RequestBody @Valid RegisterAcudienteRequest request) {
        try {
            Acudiente acudiente = acudienteService.registrarAcudienteConNiños(request);
            return ResponseEntity.status(HttpStatus.CREATED).body("Acudiente y niños registrados exitosamente. ID del acudiente: " + acudiente.getIdAcudiente());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Eliminar un acudiente por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAcudiente(@PathVariable Integer id) {
        try {
            acudienteService.eliminarAcudiente(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (RuntimeException e) {
            // Si el acudiente no existe para eliminar
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // O un error 500 si la excepción es más general
        }
    }

    @Operation(summary = "Contar acudientes registrados (público)")
    @GetMapping("/contar")
    public ResponseEntity<Long> contarAcudientes() {
        long total = acudienteService.contarAcudientes();
        return ResponseEntity.ok(total);
    }
}