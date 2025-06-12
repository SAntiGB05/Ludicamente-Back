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

        // Validar existencia del niño
        Niño niño = niñoRepository.findById(dto.getIdNiño())
                .orElseThrow(() -> new IllegalArgumentException("Niño con ID " + dto.getIdNiño() + " no existe."));

        // Validar existencia del empleado
        if (dto.getIdEmpleado() != null) {
            bitacora.setEmpleado(
                    empleadoRepository.findById(dto.getIdEmpleado())
                            .orElseThrow(() -> new IllegalArgumentException("Empleado con ID " + dto.getIdEmpleado() + " no existe."))
            );
        }

        bitacora.setNiño(niño);
        return bitacoraRepository.save(bitacora);
    }

    @Override
    public List<BitacoraDto> obtenerHistorialPorNiño(Integer idNiño) {
        Niño niño = niñoRepository.findById(idNiño)
                .orElseThrow(() -> new IllegalArgumentException("Niño con ID " + idNiño + " no existe."));

        List<Bitacora> bitacoras = bitacoraRepository.findByNiñoAndEstadoTrue(niño); // sin filtrar por estado

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
        Optional<Bitacora> bitacoraExistenteOpt = bitacoraRepository.findById(idBitacora);

        if (bitacoraExistenteOpt.isEmpty()) return Optional.empty();

        Bitacora existente = bitacoraExistenteOpt.get();

        if (bitacoraActualizada.getNiño() != null &&
                !bitacoraActualizada.getNiño().getIdNiño().equals(existente.getNiño().getIdNiño())) {
            throw new IllegalArgumentException("No se puede cambiar el niño asociado a esta bitácora.");
        }

        if (bitacoraActualizada.getEmpleado() != null &&
                !bitacoraActualizada.getEmpleado().getIdEmpleado().equals(existente.getEmpleado().getIdEmpleado())) {
            throw new IllegalArgumentException("No se puede cambiar el empleado asociado a esta bitácora.");
        }

        // 📝 Actualizar campos de contenido
        existente.setDescripcionGeneral(bitacoraActualizada.getDescripcionGeneral());
        existente.setOportunidades(bitacoraActualizada.getOportunidades());
        existente.setDebilidades(bitacoraActualizada.getDebilidades());
        existente.setAmenazas(bitacoraActualizada.getAmenazas());
        existente.setFortalezas(bitacoraActualizada.getFortalezas());
        existente.setObjetivos(bitacoraActualizada.getObjetivos());
        existente.setHabilidades(bitacoraActualizada.getHabilidades());
        existente.setSeguimiento(bitacoraActualizada.getSeguimiento());
        existente.setHistorialActividad(bitacoraActualizada.getHistorialActividad());

        return Optional.of(bitacoraRepository.save(existente));
    }

    @Override
    public Optional<Bitacora> archivarBitacora(Integer idBitacora) {
        Optional<Bitacora> bitacoraOpt = bitacoraRepository.findById(idBitacora);

        if (bitacoraOpt.isEmpty()) return Optional.empty();

        Bitacora bitacora = bitacoraOpt.get();

        Niño niñoAsociado = bitacora.getNiño();
        if (niñoAsociado == null) return Optional.empty();

        // 🔄 Buscar todas las bitácoras activas del mismo niño
        List<Bitacora> activasDelNiño = bitacoraRepository.findByNiñoAndEstadoTrue(niñoAsociado);

        // ❌ Marcar todas como inactivas
        for (Bitacora b : activasDelNiño) {
            b.setEstado(false);
        }

        // 💾 Guardar todas las bitácoras actualizadas
        bitacoraRepository.saveAll(activasDelNiño);

        return Optional.of(bitacora);
    }







}