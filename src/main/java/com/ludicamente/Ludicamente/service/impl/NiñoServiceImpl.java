package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.model.Niño;
import com.ludicamente.Ludicamente.model.Repository.NiñoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NiñoServiceImpl implements NiñoService {

    @Autowired
    private NiñoRepository niñoRepository;

    @Override
    @Operation(summary = "Crear un nuevo niño")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Niño creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    public Niño crearNiño(Niño niño) {
        return niñoRepository.save(niño);
    }

    @Override
    @Operation(summary = "Obtener todos los niños")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de niños obtenida exitosamente")
    })
    public List<Niño> listarNiños() {
        return niñoRepository.findAll();
    }

    @Override
    @Operation(summary = "Actualizar un niño existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Niño actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Niño no encontrado")
    })
    public Optional<Niño> actualizarNiño(
            @Parameter(description = "ID del niño a actualizar", example = "1") Integer id,
            Niño niñoActualizado) {

        Optional<Niño> niñoExistente = niñoRepository.findById(id);
        if (niñoExistente.isPresent()) {
            Niño niño = niñoExistente.get();
            niño.setNombre(niñoActualizado.getNombre());
            niño.setApellido(niñoActualizado.getApellido());
            niño.setSexo(niñoActualizado.getSexo());
            niño.setFechaNacimiento(niñoActualizado.getFechaNacimiento());
            niño.setAlergias(niñoActualizado.getAlergias());
            niño.setObservaciones(niñoActualizado.getObservaciones());
            niño.setFoto(niñoActualizado.getFoto());
            niño.setAcudiente(niñoActualizado.getAcudiente());
            niño.setFechaIngreso(niñoActualizado.getFechaIngreso());
            niño.setGrupoSanguineo(niñoActualizado.getGrupoSanguineo());
            return Optional.of(niñoRepository.save(niño));
        }
        return Optional.empty();
    }

    @Override
    @Operation(summary = "Eliminar un niño")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Niño eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Niño no encontrado")
    })
    public boolean eliminarNiño(@Parameter(description = "ID del niño a eliminar", example = "1") Integer id) {
        if (niñoRepository.existsById(id)) {
            niñoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
