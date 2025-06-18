package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.dto.NiñoDto;
import com.ludicamente.Ludicamente.model.Niño;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface NiñoService {

    NiñoDto crearNiño(NiñoDto niñoDto);

    List<NiñoDto> listarNiños();

    List<NiñoDto> listarTodosLosNiños();

    List<NiñoDto> listarNiñosPorCorreoAcudiente(String correoAcudiente);

    List<NiñoDto> listarNiñosPorAcudiente(Integer idAcudiente);

    Optional<NiñoDto> actualizarNiño(Integer id, NiñoDto niñoActualizado);

    Optional<NiñoDto> obtenerNiñoPorId(Integer id);

    boolean eliminarNiño(Integer id);

    void actualizarFoto(Integer id, MultipartFile foto);
}
