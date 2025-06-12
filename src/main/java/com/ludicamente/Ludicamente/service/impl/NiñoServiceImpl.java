package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.dto.NiñoDto;
import com.ludicamente.Ludicamente.model.Acudiente;
import com.ludicamente.Ludicamente.model.Bitacora;
import com.ludicamente.Ludicamente.model.Niño;
import com.ludicamente.Ludicamente.repository.AcudienteRepository;
import com.ludicamente.Ludicamente.repository.BitacoraRepository;
import com.ludicamente.Ludicamente.repository.NiñoRepository;
import com.ludicamente.Ludicamente.service.NiñoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Base64;
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

        Bitacora bitacoraInicial = new Bitacora();
        bitacoraInicial.setTitulo("Bitácora inicial");
        bitacoraInicial.setEstado(true); // activa
        bitacoraInicial.setNiño(niñoGuardado);
        bitacoraInicial.setFechaCreacion(LocalDate.now()); // Asegúrate de tener este campo en la entidad
        bitacoraRepository.save(bitacoraInicial);
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
        return niños.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NiñoDto> listarNiñosPorCorreoAcudiente(String correoAcudiente) {
        Optional<Acudiente> acudienteOpt = acudienteRepository.findByCorreo(correoAcudiente);
        if (acudienteOpt.isEmpty()) {
            return List.of(); // O podrías lanzar una excepción personalizada
        }
        Acudiente acudiente = acudienteOpt.get();
        List<Niño> niños = niñoRepository.findByAcudienteIdAcudiente(acudiente.getIdAcudiente());
        return niños.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public void actualizarFoto(Integer id, MultipartFile foto) {
        Niño niño = niñoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Niño no encontrado con ID: " + id));

        try {
            byte[] bytes = foto.getBytes();
            String base64Image = "data:" + foto.getContentType() + ";base64," +
                    Base64.getEncoder().encodeToString(bytes);

            niño.setFoto(base64Image);
            niñoRepository.save(niño);
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la imagen", e);
        }
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
            niño.setEdad(calcularEdad(niñoDto.getFechaNacimiento()));
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

    private int calcularEdad(Date fechaNacimiento) {
        LocalDate fecha = fechaNacimiento.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return Period.between(fecha, LocalDate.now()).getYears();
    }

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

        NiñoDto dto = new NiñoDto(
                niño.getIdNiño(),
                niño.getNombre(),
                niño.getnIdentificacion(),
                niño.getSexo(),
                niño.getFechaNacimiento(),
                niño.getEdad(),
                niño.getFoto(),
                idAcudiente
        );

        dto.setNombreAcudiente(nombreAcudiente);
        dto.setParentescoAcudiente(parentescoAcudiente);
        dto.setTelefonoAcudiente(telefonoAcudiente);

        // Verificar si tiene bitácora activa
        List<Bitacora> bitacorasActivas = bitacoraRepository.findByNiñoAndEstadoTrue(niño);
        dto.setBitacoraActiva(!bitacorasActivas.isEmpty());

        return dto;
    }
    @Override
    public Optional<NiñoDto> obtenerNiñoPorId(Integer id) {
        return niñoRepository.findById(id)
                .map(this::convertirADto);
    }


    private Niño convertirADominio(NiñoDto dto) {
        Niño niño = new Niño();
        niño.setIdNiño(dto.getIdNiño());
        niño.setNombre(dto.getNombre());
        niño.setnIdentificacion(dto.getnIdentificacion());
        niño.setSexo(dto.getSexo());
        niño.setFechaNacimiento(dto.getFechaNacimiento());
        niño.setFoto(dto.getFoto());
        niño.setEdad(calcularEdad(dto.getFechaNacimiento()));


        if (dto.getIdAcudiente() != null) {
            acudienteRepository.findById(dto.getIdAcudiente())
                    .ifPresent(niño::setAcudiente);
        }

        return niño;
    }
}
