package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.dto.ServicioDto;
import com.ludicamente.Ludicamente.model.Servicio;
import com.ludicamente.Ludicamente.repository.ServicioRepository;
import com.ludicamente.Ludicamente.service.ServicioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<ServicioDto> listarServicios() {
        return servicioRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ServicioDto mapToDto(Servicio servicio) {
        ServicioDto dto = new ServicioDto();
        dto.setCodServicio(servicio.getCodServicio());
        dto.setNombreServicio(servicio.getNombreServicio());
        dto.setDescripcion(servicio.getDescripcion());
        dto.setCosto(servicio.getCosto());
        dto.setDuracionMinutos(servicio.getDuracionMinutos());
        dto.setCapacidadMaxima(servicio.getCapacidadMaxima());
        dto.setRequisitos(servicio.getRequisitos());
        dto.setEstado(servicio.getEstado() != null ? servicio.getEstado().toString() : null);

        if (servicio.getCategoria() != null) {
            dto.setFkcodCategoria(servicio.getCategoria().getCodCategoria());
        } else {
            dto.setFkcodCategoria(null);
        }

        return dto;
    }

    @Override
    public Optional<Servicio> obtenerServicioPorId(Integer id) {
        return servicioRepository.findById(id);
    }

    @Override
    public Optional<Servicio> actualizarServicio(Integer id, Servicio servicioActualizado) {
        return servicioRepository.findById(id).map(servicioExistente -> {
            servicioExistente.setNombreServicio(servicioActualizado.getNombreServicio());
            servicioExistente.setDescripcion(servicioActualizado.getDescripcion());
            servicioExistente.setCosto(servicioActualizado.getCosto());
            servicioExistente.setDuracionMinutos(servicioActualizado.getDuracionMinutos());
            servicioExistente.setCapacidadMaxima(servicioActualizado.getCapacidadMaxima());
            servicioExistente.setCategoria(servicioActualizado.getCategoria());
            servicioExistente.setRequisitos(servicioActualizado.getRequisitos());
            servicioExistente.setEstado(servicioActualizado.getEstado());
            return Optional.of(servicioRepository.save(servicioExistente));
        }).orElse(Optional.empty());
    }

    @Override
    public List<Servicio> listarPorCategoria(Integer idCategoria) {
        return servicioRepository.findByCategoriaCodCategoria(idCategoria);
    }

    @Override
    public Optional<Servicio> obtenerPorId(Integer id) {
        return servicioRepository.findById(id);
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
