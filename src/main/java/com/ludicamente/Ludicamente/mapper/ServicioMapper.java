package com.ludicamente.Ludicamente.mapper;

import com.ludicamente.Ludicamente.dto.ServicioDto;
import com.ludicamente.Ludicamente.model.Categoria;
import com.ludicamente.Ludicamente.model.Servicio;

public class ServicioMapper {

    public static ServicioDto toDto(Servicio servicio) {
        String estadoString = null;
        if (servicio.getEstado() != null) {
            switch (servicio.getEstado()) {
                case disponible -> estadoString = "ACTIVO";
                case no_disponible -> estadoString = "INACTIVO";
            }
        }

        return new ServicioDto(
                servicio.getCodServicio(),
                servicio.getNombreServicio(),
                servicio.getDescripcion(),
                servicio.getCosto(),
                servicio.getDuracionMinutos(),
                servicio.getCapacidadMaxima(),
                servicio.getCategoria() != null ? servicio.getCategoria().getCodCategoria() : null,
                servicio.getRequisitos(),
                estadoString
        );
    }

    public static Servicio toEntity(ServicioDto dto, Categoria categoria) {
        Servicio.EstadoServicio estadoEnum;

        if (dto.getEstado() == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }

        switch (dto.getEstado().trim().toUpperCase()) {
            case "ACTIVO" -> estadoEnum = Servicio.EstadoServicio.disponible;
            case "INACTIVO" -> estadoEnum = Servicio.EstadoServicio.no_disponible;
            default -> throw new IllegalArgumentException("Estado inválido: " + dto.getEstado());
        }

        return new Servicio(
                dto.getNombreServicio(),
                dto.getDescripcion(),
                dto.getCosto(),
                dto.getDuracionMinutos(),
                dto.getCapacidadMaxima(),
                categoria,
                dto.getRequisitos(),
                estadoEnum
        );
    }
}
