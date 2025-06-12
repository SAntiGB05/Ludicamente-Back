package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.dto.NiñoDto;
import com.ludicamente.Ludicamente.model.Niño;

import java.util.List;
import java.util.Optional;

public interface NiñoService {
    NiñoDto crearNiño(NiñoDto niñoDto);
    List<NiñoDto> listarNiñosPorCorreoAcudiente(String correoAcudiente);
    List<NiñoDto> listarNiñosPorAcudiente(Integer idAcudiente);
    Optional<NiñoDto> actualizarNiño(Integer id, NiñoDto niñoActualizado);
    boolean eliminarNiño(Integer id);
    List<NiñoDto> listarTodosLosNiños();
    Optional<NiñoDto> obtenerNiñoPorId(Integer id);

}
