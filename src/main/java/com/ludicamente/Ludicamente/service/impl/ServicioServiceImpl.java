package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.dto.ServicioDto;
import com.ludicamente.Ludicamente.mapper.ServicioMapper;
import com.ludicamente.Ludicamente.model.Categoria;
import com.ludicamente.Ludicamente.model.Servicio;
import com.ludicamente.Ludicamente.repository.CategoriaRepository;
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
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public ServicioServiceImpl(ServicioRepository servicioRepository, CategoriaRepository categoriaRepository) {
        this.servicioRepository = servicioRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public Servicio crearServicio(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    @Override
    public List<ServicioDto> listarServicios() {
        return servicioRepository.findAll()
                .stream()
                .map(ServicioMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Servicio> obtenerServicioPorId(Integer id) {
        return servicioRepository.findById(id);
    }

    @Override
    public Optional<ServicioDto> actualizarServicio(Integer id, ServicioDto dto) {
        return servicioRepository.findById(id).map(servicioExistente -> {
            Categoria categoria = categoriaRepository.findById(dto.getFkcodCategoria())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + dto.getFkcodCategoria()));

            servicioExistente.setNombreServicio(dto.getNombreServicio());
            servicioExistente.setDescripcion(dto.getDescripcion());
            servicioExistente.setCosto(dto.getCosto());
            servicioExistente.setDuracionMinutos(dto.getDuracionMinutos());
            servicioExistente.setCapacidadMaxima(dto.getCapacidadMaxima());
            servicioExistente.setRequisitos(dto.getRequisitos());
            servicioExistente.setCategoria(categoria);

            switch (dto.getEstado().trim().toUpperCase()) {
                case "ACTIVO" -> servicioExistente.setEstado(Servicio.EstadoServicio.disponible);
                case "INACTIVO" -> servicioExistente.setEstado(Servicio.EstadoServicio.no_disponible);
                default -> throw new IllegalArgumentException("Estado inválido: " + dto.getEstado());
            }

            Servicio actualizado = servicioRepository.save(servicioExistente);
            return ServicioMapper.toDto(actualizado); // 👈 este ya es el ServicioDto, no Optional<>
        });
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