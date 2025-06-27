package com.ludicamente.Ludicamente.mapper;

import com.ludicamente.Ludicamente.dto.ServicioDto;
import com.ludicamente.Ludicamente.model.Categoria;
import com.ludicamente.Ludicamente.model.Servicio;

public class ServicioMapper {

    public static ServicioDto toDto(Servicio servicio) {
        return new ServicioDto(
                servicio.getCodServicio(),
                servicio.getNombreServicio(),
                servicio.getDescripcion(),
                servicio.getCosto(), // Ya es BigDecimal, no se necesita valueOf
                servicio.getDuracionMinutos(),
                servicio.getCapacidadMaxima(),
                servicio.getCategoria() != null ? servicio.getCategoria().getCodCategoria() : null,
                servicio.getRequisitos(),
                servicio.getEstado() != null ? servicio.getEstado().name() : null
        );
    }

    public static Servicio toEntity(ServicioDto dto, Categoria categoria) {
        Servicio.EstadoServicio estadoEnum = Servicio.EstadoServicio.fromString(dto.getEstado());

        return new Servicio(
                dto.getNombreServicio(),
                dto.getDescripcion(),
                dto.getCosto(), // Ya es BigDecimal
                dto.getDuracionMinutos(),
                dto.getCapacidadMaxima(),
                categoria,
                dto.getRequisitos(),
                estadoEnum
        );
    }
}
