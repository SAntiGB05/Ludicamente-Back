package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.model.Acudiente;
import java.util.List;

public interface AcudienteService {
    List<Acudiente> listarAcudientes();
    Acudiente obtenerAcudientePorId(Integer id);
    Acudiente guardarAcudiente(Acudiente acudiente);
    Acudiente actualizarAcudiente(Integer id, Acudiente acudienteDetails);
    void eliminarAcudiente(Integer id);

}
