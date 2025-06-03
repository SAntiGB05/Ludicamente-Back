package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.dto.BitacoraDto;
import com.ludicamente.Ludicamente.model.Bitacora;
import com.ludicamente.Ludicamente.model.Niño;

import java.util.List;
import java.util.Optional;

public interface BitacoraService {
    Bitacora crearBitacoraDesdeDto(BitacoraDto dto);
    List<Bitacora> listarBitacoras();
    Optional<Bitacora> actualizarBitacora(Integer id, Bitacora bitacoraActualizada);
    boolean eliminarBitacora(Integer id);
    List<Bitacora> findByNiñoAndEstadoTrue(Integer idNiño);

}