package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.auth.RegisterAcudienteRequest;
import com.ludicamente.Ludicamente.dto.AcudienteDto;
import com.ludicamente.Ludicamente.model.Acudiente;
import java.util.List;

public interface AcudienteService {
    List<Acudiente> listarAcudientes();
    Acudiente obtenerAcudientePorCedula(String cedula);
    Acudiente obtenerAcudientePorCorreo(String correo); // Cambiado de obtenerAcudientePorId
    Acudiente guardarAcudiente(Acudiente acudiente);
    Acudiente actualizarAcudiente(Integer id, Acudiente acudienteDetails);
    Acudiente actualizarAcudienteAdmin(Integer id, AcudienteDto acudienteDetails);
    void eliminarAcudiente(Integer id);

    Acudiente registrarAcudienteConNiños(RegisterAcudienteRequest request);

}