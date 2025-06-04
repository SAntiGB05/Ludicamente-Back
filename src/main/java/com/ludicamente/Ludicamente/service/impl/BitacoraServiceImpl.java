package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.dto.BitacoraDto;
import com.ludicamente.Ludicamente.mapper.BitacoraMapper;
import com.ludicamente.Ludicamente.model.Bitacora;
import com.ludicamente.Ludicamente.model.Niño;
import com.ludicamente.Ludicamente.repository.BitacoraRepository;
import com.ludicamente.Ludicamente.repository.EmpleadoRepository;
import com.ludicamente.Ludicamente.repository.NiñoRepository;
import com.ludicamente.Ludicamente.service.BitacoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BitacoraServiceImpl implements BitacoraService {

    private final BitacoraRepository bitacoraRepository;

    @Autowired
    private NiñoRepository niñoRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    public BitacoraServiceImpl(BitacoraRepository bitacoraRepository) {
        this.bitacoraRepository = bitacoraRepository;
    }

    @Override
    public Bitacora crearBitacoraDesdeDto(BitacoraDto dto) {
        Bitacora bitacora = BitacoraMapper.toEntity(dto);

        if (dto.getIdEmpleado() != null) {
            bitacora.setEmpleado(empleadoRepository.findById(dto.getIdEmpleado()).orElse(null));
        }

        if (dto.getIdNiño() != null) {
            bitacora.setNiño(niñoRepository.findById(dto.getIdNiño()).orElse(null));
        }

        return bitacoraRepository.save(bitacora);
    }
    @Override
    public List<BitacoraDto> obtenerHistorialPorNiño(Integer idNiño) {
        Optional<Niño> niñoOpt = niñoRepository.findById(idNiño);
        if (niñoOpt.isEmpty()) return List.of();

        List<Bitacora> bitacoras = bitacoraRepository.findByNiñoAndEstadoTrue(niñoOpt.get());
        return bitacoras.stream()
                .map(BitacoraMapper::toDto)
                .collect(Collectors.toList());
    }



    @Override
    public List<Bitacora> listarBitacoras() {
        return bitacoraRepository.findAll();
    }

    @Override
    public List<Bitacora> findByNiñoAndEstadoTrue(Integer idNiño) {
        Optional<Niño> niñoOptional = niñoRepository.findById(idNiño);
        if (niñoOptional.isPresent()) {
            return bitacoraRepository.findByNiñoAndEstadoTrue(niñoOptional.get());
        } else {
            return Collections.emptyList(); // o lanzar excepción si prefieres
        }
    }



    @Override
    public Optional<Bitacora> actualizarBitacora(Integer idBitacora, Bitacora bitacoraActualizada) {
        Optional<Bitacora> bitacoraExistente = bitacoraRepository.findById(idBitacora);
        if (bitacoraExistente.isPresent()) {
            Bitacora bitacora = bitacoraExistente.get();

            // Set campos a actualizar
            bitacora.setDescripcionGeneral(bitacoraActualizada.getDescripcionGeneral());
            bitacora.setOportunidades(bitacoraActualizada.getOportunidades());
            bitacora.setDebilidades(bitacoraActualizada.getDebilidades());
            bitacora.setAmenazas(bitacoraActualizada.getAmenazas());
            bitacora.setFortalezas(bitacoraActualizada.getFortalezas());
            bitacora.setObjetivos(bitacoraActualizada.getObjetivos());
            bitacora.setHabilidades(bitacoraActualizada.getHabilidades());
            bitacora.setSeguimiento(bitacoraActualizada.getSeguimiento());
            bitacora.setHistorialActividad(bitacoraActualizada.getHistorialActividad());
            bitacora.setEmpleado(bitacoraActualizada.getEmpleado());

            return Optional.of(bitacoraRepository.save(bitacora));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Bitacora> archivarBitacora(Integer idBitacora) {
        Optional<Bitacora> bitacoraOpt = bitacoraRepository.findById(idBitacora);
        if (bitacoraOpt.isPresent()) {
            Bitacora bitacora = bitacoraOpt.get();
            bitacora.setEstado(false); // Marcar como inactiva
            return Optional.of(bitacoraRepository.save(bitacora));
        }
        return Optional.empty();
    }






}