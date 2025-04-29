package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.model.Servicio;
import com.ludicamente.Ludicamente.repository.ServicioRepository;
import com.ludicamente.Ludicamente.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;

    @Autowired
    public ServicioServiceImpl(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Override
    public Servicio crearServicio(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    @Override
    public List<Servicio> listarServicios() {
        return servicioRepository.findAll();
    }

    @Override
    public Optional<Servicio> obtenerServicioPorId(Integer id) {
        return servicioRepository.findById(id);
    }

    @Override
    public Optional<Servicio> actualizarServicio(Integer id, Servicio servicioActualizado) {
        Optional<Servicio> servicioExistente = servicioRepository.findById(id);
        if (servicioExistente.isPresent()) {
            Servicio servicio = servicioExistente.get();
            servicio.setNombreServicio(servicioActualizado.getNombreServicio());
            servicio.setDescripcion(servicioActualizado.getDescripcion());
            servicio.setCosto(servicioActualizado.getCosto());
            servicio.setDuracionMinutos(servicioActualizado.getDuracionMinutos());
            servicio.setCapacidadMaxima(servicioActualizado.getCapacidadMaxima());
            servicio.setCategoria(servicioActualizado.getCategoria());
            servicio.setRequisitos(servicioActualizado.getRequisitos());
            servicio.setEstado(servicioActualizado.getEstado());
            return Optional.of(servicioRepository.save(servicio));
        }
        return Optional.empty();
    }

    @Override
    public boolean eliminarServicio(Integer id) {
        if (servicioRepository.existsById(id)) {
            servicioRepository.deleteById(id);
            return true;
        }
        return false;
    }
}