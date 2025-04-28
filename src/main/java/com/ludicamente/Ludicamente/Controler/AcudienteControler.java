package com.ludicamente.Ludicamente.Controler;

import com.ludicamente.Ludicamente.model.Acudiente;
import com.ludicamente.Ludicamente.model.Repository.AcudienteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/acudiente")
@Tag(name = "Acudiente", description = "API para gestionar acudientes")
public class AcudienteControler {

    @Autowired
    private AcudienteRepository acudienteRepository;

    @Operation(summary = "Listar todos los acudientes")
    @GetMapping
    public List<Acudiente> getAllAcudientes() {
        return acudienteRepository.findAll();
    }

    @Operation(summary = "Obtener un acudiente por ID")
    @GetMapping("/{id}")
    public Optional<Acudiente> getAcudienteById(@PathVariable Long id) {
        return acudienteRepository.findById(id);
    }

    @Operation(summary = "Crear un nuevo acudiente")
    @PostMapping
    public Acudiente createAcudiente(@RequestBody Acudiente acudiente) {
        return acudienteRepository.save(acudiente);
    }

    @Operation(summary = "Actualizar un acudiente existente")
    @PutMapping("/{id}")
    public Acudiente updateAcudiente(@PathVariable Long id, @RequestBody Acudiente acudienteDetails) {
        Acudiente acudiente = acudienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Acudiente no encontrado con id: " + id));

        acudiente.setNombre(acudienteDetails.getNombre());
        acudiente.setCedula(acudienteDetails.getCedula());
        acudiente.setCorreo(acudienteDetails.getCorreo());
        acudiente.setContraseña(acudienteDetails.getContraseña());
        acudiente.setTelefono(acudienteDetails.getTelefono());
        acudiente.setParentesco(acudienteDetails.getParentesco());
        acudiente.setDireccion(acudienteDetails.getDireccion());

        return acudienteRepository.save(acudiente);
    }

    @Operation(summary = "Eliminar un acudiente")
    @DeleteMapping("/{id}")
    public void deleteAcudiente(@PathVariable Long id) {
        acudienteRepository.deleteById(id);
    }

}
