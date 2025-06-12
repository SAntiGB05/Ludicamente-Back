package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.model.Niño;
import com.ludicamente.Ludicamente.repository.NiñoRepository;
import com.ludicamente.Ludicamente.service.NiñoService;
import com.ludicamente.Ludicamente.dto.NiñoDto; // <--- Importa el DTO
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Necesario para el stream() y collect()

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
    public List<NiñoDto> listarNiños() { // <--- El tipo de retorno ahora es List<NiñoDto>
        return niñoRepository.findAll().stream()
                .map(this::convertToDto) // <--- Mapeamos cada Niño a NiñoDto
                .collect(Collectors.toList());
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
            niño.setnIdentificacion(niñoActualizado.getnIdentificacion());
            niño.setSexo(niñoActualizado.getSexo());
            niño.setFechaNacimiento(niñoActualizado.getFechaNacimiento());
            niño.setEdad(niñoActualizado.getEdad());
            niño.setFoto(niñoActualizado.getFoto());
            niño.setAcudiente(niñoActualizado.getAcudiente());
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

    /**
     * Convierte una entidad Niño a un NiñoDto, incluyendo la cédula del acudiente.
     * @param niño La entidad Niño a convertir.
     * @return El NiñoDto resultante.
     */
    private NiñoDto convertToDto(Niño niño) {
        NiñoDto dto = new NiñoDto();
        dto.setIdNiño(niño.getIdNiño());
        dto.setNombre(niño.getNombre());
        dto.setnIdentificacion(niño.getnIdentificacion());
        dto.setSexo(niño.getSexo());
        dto.setFechaNacimiento(niño.getFechaNacimiento());
        dto.setEdad(niño.getEdad());
        dto.setFoto(niño.getFoto());

        // Asegúrate de que el acudiente no sea nulo antes de intentar acceder a sus propiedades
        if (niño.getAcudiente() != null) {
            dto.setIdAcudiente(niño.getAcudiente().getIdAcudiente());
            dto.setCedulaAcudiente(niño.getAcudiente().getCedula()); // <--- ¡Aquí se obtiene la cédula del Acudiente!
        }
        return dto;
    }
}