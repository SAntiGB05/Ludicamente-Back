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

        // Validar existencia del empleado (opcional)
        if (dto.getIdEmpleado() != null) {
            bitacora.setEmpleado(
                    empleadoRepository.findById(dto.getIdEmpleado())
                            .orElseThrow(() -> new IllegalArgumentException("Empleado con ID " + dto.getIdEmpleado() + " no existe."))
            );
        }

        // Asociar niño
        bitacora.setNiño(niño);

        // ✅ Asignar textos por defecto para los campos de contenido
        bitacora.setDescripcionGeneral("Aquí va la descripción general del pequeño genio.");
        bitacora.setOportunidades("Aquí van las oportunidades del pequeño genio.");
        bitacora.setDebilidades("Aquí van las debilidades del pequeño genio.");
        bitacora.setFortalezas("Aquí van las fortalezas del pequeño genio.");
        bitacora.setAmenazas("Aquí van las amenazas del pequeño genio.");
        bitacora.setObjetivos("Aquí van los objetivos del pequeño genio.");
        bitacora.setHabilidades("Aquí van las habilidades del pequeño genio.");
        bitacora.setSeguimiento("Aquí va el seguimiento del pequeño genio.");

        return bitacoraRepository.save(bitacora);
    }

    @Override
    public List<BitacoraDto> obtenerHistorialPorNiño(Integer idNiño) {
        Niño niño = niñoRepository.findById(idNiño)
                .orElseThrow(() -> new IllegalArgumentException("Niño con ID " + idNiño + " no existe."));

        List<Bitacora> bitacoras = bitacoraRepository.findByNiño(niño); // sin filtrar por estado

        return bitacoras.stream()
                .map(BitacoraMapper::toDto)
                .collect(Collectors.toList());
    }



    @Override
    public Optional<Bitacora> findByNiñoAndCodBitacora(Integer idNiño, Integer codBitacora) {
        Optional<Niño> niñoOpt = niñoRepository.findById(idNiño);
        if (niñoOpt.isEmpty()) {
            return Optional.empty();
        }
        return bitacoraRepository.findByNiñoAndCodBitacora(niñoOpt.get(), codBitacora);
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

        // Validaciones de integridad
        if (bitacoraActualizada.getNiño() != null &&
                !bitacoraActualizada.getNiño().getIdNiño().equals(existente.getNiño().getIdNiño())) {
            throw new IllegalArgumentException("No se puede cambiar el niño asociado a esta bitácora.");
        }

        if (bitacoraActualizada.getEmpleado() != null &&
                !bitacoraActualizada.getEmpleado().getIdEmpleado().equals(existente.getEmpleado().getIdEmpleado())) {
            throw new IllegalArgumentException("No se puede cambiar el empleado asociado a esta bitácora.");
        }

        if (bitacoraActualizada.getTitulo() != null) {
            existente.setTitulo(bitacoraActualizada.getTitulo());
        }

        if (bitacoraActualizada.getEstado() != null) {
            existente.setEstado(bitacoraActualizada.getEstado());
        }

        if (bitacoraActualizada.getDescripcionGeneral() != null) {
            existente.setDescripcionGeneral(bitacoraActualizada.getDescripcionGeneral());
        }

        if (bitacoraActualizada.getOportunidades() != null) {
            existente.setOportunidades(bitacoraActualizada.getOportunidades());
        }

        if (bitacoraActualizada.getDebilidades() != null) {
            existente.setDebilidades(bitacoraActualizada.getDebilidades());
        }

        if (bitacoraActualizada.getAmenazas() != null) {
            existente.setAmenazas(bitacoraActualizada.getAmenazas());
        }

        if (bitacoraActualizada.getFortalezas() != null) {
            existente.setFortalezas(bitacoraActualizada.getFortalezas());
        }

        if (bitacoraActualizada.getObjetivos() != null) {
            existente.setObjetivos(bitacoraActualizada.getObjetivos());
        }

        if (bitacoraActualizada.getHabilidades() != null) {
            existente.setHabilidades(bitacoraActualizada.getHabilidades());
        }

        if (bitacoraActualizada.getSeguimiento() != null) {
            existente.setSeguimiento(bitacoraActualizada.getSeguimiento());
        }

        if (bitacoraActualizada.getHistorialActividad() != null) {
            existente.setHistorialActividad(bitacoraActualizada.getHistorialActividad());
        }

        return Optional.of(bitacoraRepository.save(existente));
    }

    @Override
    public void guardarTodas(List<Bitacora> bitacoras) {
        bitacoraRepository.saveAll(bitacoras);
    }

    @Override
    public void activarTodasPorNiño(Integer idNiño) {
        Optional<Niño> niñoOpt = niñoRepository.findById(idNiño.intValue()); // o idNiño si es Integer
        if (niñoOpt.isEmpty()) {
            throw new IllegalArgumentException("Niño con ID " + idNiño + " no existe.");
        }

        List<Bitacora> bitacoras = bitacoraRepository.findByNiño(niñoOpt.get());
        for (Bitacora bitacora : bitacoras) {
            bitacora.setEstado(true);
        }
        bitacoraRepository.saveAll(bitacoras);
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