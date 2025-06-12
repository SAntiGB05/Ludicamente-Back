package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.dto.BitacoraDto;
import com.ludicamente.Ludicamente.model.Bitacora;
import com.ludicamente.Ludicamente.model.Niño;

import java.util.List;
import java.util.Optional;

public interface BitacoraService {
    Bitacora crearBitacoraDesdeDto(BitacoraDto dto);
    List<Bitacora> listarBitacoras();
    Optional<Bitacora> actualizarBitacora(Integer idNiño, Bitacora bitacoraActualizada);
    List<Bitacora> findByNiñoAndEstadoTrue(Integer idNiño);
    Optional<Bitacora> archivarBitacora(Integer idBitacora);
    List<BitacoraDto> obtenerHistorialPorNiño(Integer idNiño);
    Optional<Bitacora> findByNiñoAndCodBitacora(Integer idNiño, Integer codBitacora);
    void guardarTodas(List<Bitacora> bitacoras);
    public void activarTodasPorNiño(Integer idNiño);




}