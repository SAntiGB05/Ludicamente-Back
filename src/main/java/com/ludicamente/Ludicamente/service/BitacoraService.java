package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.model.Bitacora;

import java.util.List;
import java.util.Optional;

public interface BitacoraService {
    Bitacora crearBitacora(Bitacora bitacora);
    List<Bitacora> listarBitacoras();
    Optional<Bitacora> actualizarBitacora(Integer id, Bitacora bitacoraActualizada);
    boolean eliminarBitacora(Integer id);
}