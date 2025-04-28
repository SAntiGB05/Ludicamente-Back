package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.model.Acudiente;
import com.ludicamente.Ludicamente.service.AcudienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/acudiente")
@Tag(name = "Acudiente", description = "API para gestionar acudientes")
public class AcudienteController {

    @Autowired
    private AcudienteService acudienteService;

    @Operation(summary = "Listar todos los acudientes")
    @GetMapping
    public List<Acudiente> listarAcudientes() {
        return acudienteService.listarAcudientes();
    }

    @Operation(summary = "Obtener un acudiente por ID")
    @GetMapping("/{id}")
    public Acudiente getAcudienteById(@PathVariable Integer id) {
        return acudienteService.obtenerAcudientePorId(id);
    }

    @Operation(summary = "Crear un nuevo acudiente")
    @PostMapping
    public Acudiente createAcudiente(@RequestBody Acudiente acudiente) {
        return acudienteService.guardarAcudiente(acudiente);
    }

    @Operation(summary = "Actualizar un acudiente existente")
    @PutMapping("/{id}")
    public Acudiente updateAcudiente(@PathVariable Integer id, @RequestBody Acudiente acudienteDetails) {
        return acudienteService.actualizarAcudiente(id, acudienteDetails);
    }

    @Operation(summary = "Eliminar un acudiente")
    @DeleteMapping("/{id}")
    public void deleteAcudiente(@PathVariable Integer id) {
        acudienteService.eliminarAcudiente(id);
    }

}
