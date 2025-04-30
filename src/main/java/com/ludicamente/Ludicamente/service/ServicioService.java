package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.model.Servicio;

import java.util.List;
import java.util.Optional;

public interface ServicioService {

    // Crear un nuevo servicio
    Servicio crearServicio(Servicio servicio);

    // Obtener todos los servicios
    List<Servicio> listarServicios();

    // Obtener un servicio por su ID
    Optional<Servicio> obtenerServicioPorId(Integer id);

    // Actualizar un servicio existente
    Optional<Servicio> actualizarServicio(Integer id, Servicio servicioActualizado);

    // Eliminar un servicio por su ID
    boolean eliminarServicio(Integer id);
}