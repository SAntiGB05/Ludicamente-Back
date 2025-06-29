package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.dto.ServicioDto;
import com.ludicamente.Ludicamente.model.Categoria;
import com.ludicamente.Ludicamente.model.Servicio;

import java.util.List;
import java.util.Optional;

public interface ServicioService {

    Servicio crearServicio(Servicio servicio);
    List<ServicioDto> listarServicios(); // Cambiado a DTO
    Optional<Servicio> obtenerServicioPorId(Integer id);
    Optional<Servicio> actualizarServicio(Integer id, Servicio servicioActualizado);
    boolean eliminarServicio(Integer id);
    List<Servicio> listarPorCategoria(Integer idCategoria);
    Optional<Servicio> obtenerPorId(Integer id);



}