package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.auth.userdetails.AcudienteUserDetails;
import com.ludicamente.Ludicamente.model.Acudiente;
import com.ludicamente.Ludicamente.service.AcudienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Listar acudientes")
    @GetMapping
    public List<Acudiente> listarAcudientes() {
        return acudienteService.listarAcudientes();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ACUDIENTE')")
    @GetMapping("/correo/{correo}")
    public ResponseEntity<Acudiente> getAcudienteByCorreo(@PathVariable String correo, Authentication authentication) {
        // Verificar que el correo solicitado coincide con el usuario autenticado
        if (!correo.equals(authentication.getName())) {
            return ResponseEntity.status(403).build(); // 403 Forbidden
        }

        try {
            Acudiente acudiente = acudienteService.obtenerAcudientePorCorreo(correo);
            return ResponseEntity.ok(acudiente);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).build(); // 404 Not Found
        }
    }

    @Operation(summary = "Crear un nuevo acudiente")
    @PostMapping
    public Acudiente createAcudiente(@RequestBody Acudiente acudiente) {
        return acudienteService.guardarAcudiente(acudiente);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'ACUDIENTE')")
    @Operation(summary = "Actualizar un acudiente existente")
    @PutMapping("/{id}")
    public Acudiente updateAcudiente(@PathVariable Integer id, @RequestBody Acudiente acudienteDetails) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof AcudienteUserDetails acudienteUserDetails) {
            Acudiente acudiente = acudienteUserDetails.getAcudiente();
            if (!acudiente.getIdAcudiente().equals(id)) {
                throw new AccessDeniedException("No tienes permiso para actualizar este acudiente");
            }
        }
        return acudienteService.actualizarAcudiente(id, acudienteDetails);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Eliminar un acudiente")
    @DeleteMapping("/{id}")
    public void deleteAcudiente(@PathVariable Integer id) {
        acudienteService.eliminarAcudiente(id);
    }
}