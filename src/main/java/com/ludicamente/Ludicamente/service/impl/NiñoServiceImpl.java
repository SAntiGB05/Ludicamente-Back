package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.dto.NiñoDto;
import com.ludicamente.Ludicamente.model.Acudiente;
import com.ludicamente.Ludicamente.model.Bitacora;
import com.ludicamente.Ludicamente.model.Niño;
import com.ludicamente.Ludicamente.repository.AcudienteRepository;
import com.ludicamente.Ludicamente.repository.BitacoraRepository;
import com.ludicamente.Ludicamente.repository.NiñoRepository;
import com.ludicamente.Ludicamente.service.NiñoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Crear un nuevo niño")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Niño creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    public NiñoDto crearNiño(NiñoDto niñoDto) {
        Niño niño = convertirADominio(niñoDto);
        Niño niñoGuardado = niñoRepository.save(niño);

        Bitacora bitacoraInicial = new Bitacora();
        bitacoraInicial.setTitulo("Bitácora inicial");
        bitacoraInicial.setEstado(true);
        bitacoraInicial.setNiño(niñoGuardado);
        bitacoraInicial.setFechaCreacion(LocalDate.now());
        bitacoraRepository.save(bitacoraInicial);
        return convertirADto(niñoGuardado);
    }

    @Override
    @Operation(summary = "Obtener todos los niños")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de niños obtenida exitosamente")
    })
    public List<NiñoDto> listarNiños() {
        return niñoRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NiñoDto> listarNiñosPorAcudiente(Integer idAcudiente) {
        List<Niño> niños = niñoRepository.findByAcudienteIdAcudiente(idAcudiente);
        return niños.stream().map(this::convertirADto).collect(Collectors.toList());
    }

    @Override
    public List<NiñoDto> listarTodosLosNiños() {
        List<Niño> niños = niñoRepository.findAll();
        return niños.stream().map(this::convertirADto).collect(Collectors.toList());
    }

    @Override
    public List<NiñoDto> listarNiñosPorCorreoAcudiente(String correoAcudiente) {
        Optional<Acudiente> acudienteOpt = acudienteRepository.findByCorreo(correoAcudiente);
        if (acudienteOpt.isEmpty()) return List.of();
        List<Niño> niños = niñoRepository.findByAcudienteIdAcudiente(acudienteOpt.get().getIdAcudiente());
        return niños.stream().map(this::convertirADto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void actualizarFoto(Integer id, MultipartFile foto) {
        Niño niño = niñoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Niño no encontrado con ID: " + id));
        try {
            byte[] bytes = foto.getBytes();
            String base64Image = "data:" + foto.getContentType() + ";base64," + Base64.getEncoder().encodeToString(bytes);
            niño.setFoto(base64Image);
            niñoRepository.save(niño);
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la imagen", e);
        }
    }

    @Override
    @Operation(summary = "Actualizar un niño existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Niño actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Niño no encontrado")
    })
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
                acudienteRepository.findById(niñoDto.getIdAcudiente()).ifPresent(niño::setAcudiente);
            }
            return Optional.of(convertirADto(niñoRepository.save(niño)));
        }
        return Optional.empty();
    }

    @Override
    @Operation(summary = "Eliminar un niño")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Niño eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Niño no encontrado")
    })
    public boolean eliminarNiño(Integer id) {
        if (niñoRepository.existsById(id)) {
            niñoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<NiñoDto> obtenerNiñoPorId(Integer id) {
        return niñoRepository.findById(id).map(this::convertirADto);
    }

    private int calcularEdad(Date fechaNacimiento) {
        LocalDate fecha = fechaNacimiento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Period.between(fecha, LocalDate.now()).getYears();
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
            acudienteRepository.findById(dto.getIdAcudiente()).ifPresent(niño::setAcudiente);
        }
        return niño;
    }

    private NiñoDto convertirADto(Niño niño) {
        NiñoDto dto = new NiñoDto();
        dto.setIdNiño(niño.getIdNiño());
        dto.setNombre(niño.getNombre());
        dto.setnIdentificacion(niño.getnIdentificacion());
        dto.setSexo(niño.getSexo());
        dto.setFechaNacimiento(niño.getFechaNacimiento());
        dto.setEdad(niño.getEdad());
        dto.setFoto(niño.getFoto());
        if (niño.getAcudiente() != null) {
            dto.setIdAcudiente(niño.getAcudiente().getIdAcudiente());
            dto.setCedulaAcudiente(niño.getAcudiente().getCedula());
            dto.setNombreAcudiente(niño.getAcudiente().getNombre());
            dto.setParentescoAcudiente(niño.getAcudiente().getParentesco());
            dto.setTelefonoAcudiente(niño.getAcudiente().getTelefono());
        }
        List<Bitacora> bitacorasActivas = bitacoraRepository.findByNiñoAndEstadoTrue(niño);
        dto.setBitacoraActiva(!bitacorasActivas.isEmpty());
        return dto;
    }
}