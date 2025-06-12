package com.ludicamente.Ludicamente.mapper;

import com.ludicamente.Ludicamente.dto.BitacoraDto;
import com.ludicamente.Ludicamente.model.Bitacora;

public class BitacoraMapper {

    public static BitacoraDto toDto(Bitacora b) {
        BitacoraDto dto = new BitacoraDto();
        dto.setTitulo(b.getTitulo());
        dto.setFechaCreacion(b.getFechaCreacion());
        dto.setCodBitacora(b.getCodBitacora());
        dto.setDescripcionGeneral(b.getDescripcionGeneral());
        dto.setOportunidades(b.getOportunidades());
        dto.setDebilidades(b.getDebilidades());
        dto.setAmenazas(b.getAmenazas());
        dto.setFortalezas(b.getFortalezas());
        dto.setObjetivos(b.getObjetivos());
        dto.setHabilidades(b.getHabilidades());
        dto.setSeguimiento(b.getSeguimiento());
        dto.setHistorialActividad(b.getHistorialActividad());
        dto.setEstado(b.getEstado());
        dto.setIdEmpleado(b.getEmpleado() != null ? b.getEmpleado().getIdEmpleado() : null);
        dto.setIdNiño(b.getNiño() != null ? b.getNiño().getIdNiño() : null);
        return dto;
    }

    public static Bitacora toEntity(BitacoraDto dto) {
        Bitacora b = new Bitacora();
        b.setDescripcionGeneral(dto.getDescripcionGeneral());
        b.setOportunidades(dto.getOportunidades());
        b.setDebilidades(dto.getDebilidades());
        b.setAmenazas(dto.getAmenazas());
        b.setFortalezas(dto.getFortalezas());
        b.setObjetivos(dto.getObjetivos());
        b.setHabilidades(dto.getHabilidades());
        b.setSeguimiento(dto.getSeguimiento());
        b.setHistorialActividad(dto.getHistorialActividad());
        b.setEstado(true); // por defecto activa
        return b;
    }
}
