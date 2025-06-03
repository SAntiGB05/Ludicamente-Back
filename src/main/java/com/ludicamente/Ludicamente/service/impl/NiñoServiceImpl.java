package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.dto.BitacoraDto;
import com.ludicamente.Ludicamente.dto.NiñoDto;
import com.ludicamente.Ludicamente.model.Acudiente;
import com.ludicamente.Ludicamente.model.Bitacora;
import com.ludicamente.Ludicamente.model.Niño;
import com.ludicamente.Ludicamente.repository.AcudienteRepository;
import com.ludicamente.Ludicamente.repository.BitacoraRepository;
import com.ludicamente.Ludicamente.repository.NiñoRepository;
import com.ludicamente.Ludicamente.service.NiñoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NiñoServiceImpl implements NiñoService {

    @Autowired
    private NiñoRepository niñoRepository;

    @Autowired
    private AcudienteRepository acudienteRepository;

    @Autowired
    private BitacoraRepository bitacoraRepository;

    @Override
    public NiñoDto crearNiño(NiñoDto niñoDto) {
        Niño niño = convertirADominio(niñoDto);
        Niño niñoGuardado = niñoRepository.save(niño);
        return convertirADto(niñoGuardado);
    }

    @Override
    public List<NiñoDto> listarNiñosPorAcudiente(Integer idAcudiente) {
        List<Niño> niños = niñoRepository.findByAcudienteIdAcudiente(idAcudiente);
        return niños.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NiñoDto> listarTodosLosNiños() {
        System.out.println(">>> LISTANDO TODOS LOS NIÑOS (admin/empleado)");
        List<Niño> niños = niñoRepository.findAll();
        System.out.println("NIÑOS ENCONTRADOS (ADMIN/EMPLEADO): " + niños.size());
        return niños.stream().map(this::convertirADto).collect(Collectors.toList());
    }

    @Override
    public List<NiñoDto> listarNiñosPorCorreoAcudiente(String correoAcudiente) {
        Optional<Acudiente> acudienteOpt = acudienteRepository.findByCorreo(correoAcudiente);
        if (acudienteOpt.isEmpty()) {
            return List.of(); // o lanzar excepción
        }
        Acudiente acudiente = acudienteOpt.get();
        List<Niño> niños = niñoRepository.findByAcudienteIdAcudiente(acudiente.getIdAcudiente());
        return niños.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<NiñoDto> actualizarNiño(Integer id, NiñoDto niñoDto) {
        Optional<Niño> niñoExistente = niñoRepository.findById(id);

        if (niñoExistente.isPresent()) {
            Niño niño = niñoExistente.get();
            niño.setNombre(niñoDto.getNombre());
            niño.setnIdentificacion(niñoDto.getnIdentificacion());
            niño.setSexo(niñoDto.getSexo());
            niño.setFechaNacimiento(niñoDto.getFechaNacimiento());
            niño.setFoto(niñoDto.getFoto());

            if (niñoDto.getIdAcudiente() != null) {
                acudienteRepository.findById(niñoDto.getIdAcudiente())
                        .ifPresent(niño::setAcudiente);
            }

            Niño actualizado = niñoRepository.save(niño);
            return Optional.of(convertirADto(actualizado));
        }
        return Optional.empty();
    }

    @Override
    public boolean eliminarNiño(Integer id) {
        if (niñoRepository.existsById(id)) {
            niñoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // ✅ Método para calcular la edad desde la fecha de nacimiento
    private int calcularEdad(Date fechaNacimiento) {
        LocalDate fecha = fechaNacimiento.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return Period.between(fecha, LocalDate.now()).getYears();
    }

    // ✅ Convertir entidad Niño a DTO, calculando edad automáticamente
    private NiñoDto convertirADto(Niño niño) {
        Integer idAcudiente = null;
        String nombreAcudiente = null;
        String parentescoAcudiente = null;
        String telefonoAcudiente = null;

        if (niño.getAcudiente() != null) {
            idAcudiente = niño.getAcudiente().getIdAcudiente();
            nombreAcudiente = niño.getAcudiente().getNombre();
            parentescoAcudiente = niño.getAcudiente().getParentesco();
            telefonoAcudiente = niño.getAcudiente().getTelefono();
        }

        int edadCalculada = calcularEdad(niño.getFechaNacimiento());

        NiñoDto dto = new NiñoDto(
                niño.getIdNiño(),
                niño.getNombre(),
                niño.getnIdentificacion(),
                niño.getSexo(),
                niño.getFechaNacimiento(),
                edadCalculada, // 👈 Edad calculada aquí
                niño.getFoto(),
                idAcudiente
        );

        dto.setNombreAcudiente(nombreAcudiente);
        dto.setParentescoAcudiente(parentescoAcudiente);
        dto.setTelefonoAcudiente(telefonoAcudiente);

        List<Bitacora> bitacorasActivas = bitacoraRepository.findByNiñoAndEstadoTrue(niño);
        dto.setBitacoraActiva(!bitacorasActivas.isEmpty());

        return dto;
    }

    // ✅ Convertir DTO a entidad Niño, sin establecer edad manualmente
    private Niño convertirADominio(NiñoDto dto) {
        Niño niño = new Niño();
        niño.setIdNiño(dto.getIdNiño());
        niño.setNombre(dto.getNombre());
        niño.setnIdentificacion(dto.getnIdentificacion());
        niño.setSexo(dto.getSexo());
        niño.setFechaNacimiento(dto.getFechaNacimiento());
        niño.setFoto(dto.getFoto());

        // ❌ No se establece la edad manualmente

        if (dto.getIdAcudiente() != null) {
            Optional<Acudiente> acudienteOpt = acudienteRepository.findById(dto.getIdAcudiente());
            acudienteOpt.ifPresent(niño::setAcudiente);
        }

        return niño;
    }
}
