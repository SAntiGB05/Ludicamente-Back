package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.dto.NiñoDto; // <--- Importa el DTO
import com.ludicamente.Ludicamente.model.Niño;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface NiñoService {
    Niño crearNiño(Niño niño);
    List<NiñoDto> listarNiños(); // <--- Ahora retorna una lista de NiñoDto
    Optional<Niño> actualizarNiño(Integer id, Niño niñoActualizado);
    boolean eliminarNiño(Integer id);
}
    List<NiñoDto> listarNiñosPorCorreoAcudiente(String correoAcudiente);
    List<NiñoDto> listarNiñosPorAcudiente(Integer idAcudiente);
    Optional<NiñoDto> actualizarNiño(Integer id, NiñoDto niñoActualizado);
    Optional<NiñoDto> obtenerNiñoPorId(Integer id);
    void actualizarFoto(Integer id, MultipartFile foto);


}
