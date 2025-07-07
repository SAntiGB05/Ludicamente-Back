package com.ludicamente.Ludicamente.service.impl;
import com.ludicamente.Ludicamente.dto.NiñoDto;
import com.ludicamente.Ludicamente.model.Acudiente;
import com.ludicamente.Ludicamente.model.Bitacora;
import com.ludicamente.Ludicamente.model.Niño;
import com.ludicamente.Ludicamente.repository.AcudienteRepository;
import com.ludicamente.Ludicamente.repository.BitacoraRepository;
import com.ludicamente.Ludicamente.repository.NiñoRepository;
import com.ludicamente.Ludicamente.service.NiñoService;
import jakarta.transaction.Transactional; // Ojo: Usar org.springframework.transaction.annotation.Transactional si usas Spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId; // No parece usarse
import java.util.*;
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
    @Transactional
    public NiñoDto crearNiño(NiñoDto niñoDto) {
        // 1. Validación explícita antes de intentar buscar el acudiente
        if (niñoDto.getIdAcudiente() == null) {
            throw new IllegalArgumentException("El ID del acudiente es obligatorio para crear un niño.");
        }

        // Buscar el Acudiente. Si no se encuentra, lanzar una excepción.
        // Esto asegura que 'niño.setAcudiente(acudiente);' siempre tendrá un objeto no nulo.
        Acudiente acudiente = acudienteRepository.findById(niñoDto.getIdAcudiente())
                .orElseThrow(() -> new RuntimeException("Acudiente no encontrado con ID: " + niñoDto.getIdAcudiente()));

        Niño niño = convertirADominio(niñoDto); // Tu método convertirADominio ahora se encargará de otros mapeos

        // Asignar el acudiente encontrado
        niño.setAcudiente(acudiente); // <--- ¡Aseguramos que el acudiente siempre se asigna aquí!

        Niño niñoGuardado = niñoRepository.save(niño);

        // Crear y guardar la bitácora inicial
        Bitacora bitacoraInicial = new Bitacora();
        bitacoraInicial.setTitulo("Bitácora inicial");
        bitacoraInicial.setEstado(true); // activa
        bitacoraInicial.setNiño(niñoGuardado);
        bitacoraInicial.setFechaCreacion(LocalDate.now());
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
            // Considera lanzar una excepción NotFound si la ausencia del acudiente es un error
            // o simplemente devolver una lista vacía, dependiendo de tu lógica de negocio.
            return List.of();
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
    @Transactional // Agrega @Transactional para operaciones de actualización
    public Optional<NiñoDto> actualizarNiño(Integer id, NiñoDto niñoDto) {
        Optional<Niño> niñoExistente = niñoRepository.findById(id);
        if (niñoExistente.isPresent()) {
            Niño niño = niñoExistente.get();
            niño.setNombre(niñoDto.getNombre());
            niño.setnIdentificacion(niñoDto.getnIdentificacion());
            niño.setSexo(niñoDto.getSexo());
            niño.setFechaNacimiento(niñoDto.getFechaNacimiento());
            niño.setEdad(calcularEdad(niñoDto.getFechaNacimiento()));
            // Solo actualiza la foto si se proporciona una URL no nula y no vacía
            if (niñoDto.getFoto() != null && !niñoDto.getFoto().isEmpty()) {
                niño.setFoto(niñoDto.getFoto());
            }


            // Actualizar el acudiente si el ID proporcionado es diferente o el acudiente actual es nulo
            if (niñoDto.getIdAcudiente() != null) {
                if (niño.getAcudiente() == null || !niño.getAcudiente().getIdAcudiente().equals(niñoDto.getIdAcudiente())) {
                    Acudiente nuevoAcudiente = acudienteRepository.findById(niñoDto.getIdAcudiente())
                            .orElseThrow(() -> new RuntimeException("Acudiente no encontrado con ID: " + niñoDto.getIdAcudiente()));
                    niño.setAcudiente(nuevoAcudiente);
                }
            } else {
                // Si idAcudiente es null en el DTO de actualización y tu columna es not-nullable,
                // esto podría causar un error. Considera qué quieres hacer en este caso:
                // - Mantener el acudiente existente si idAcudiente es null
                // - Lanzar un error
                // - Permitir cambiar el acudiente a null (si tu DB lo permite)
                // Por ahora, asumimos que si es null, no se debe cambiar el acudiente.
                // Si la columna es nullable=false, NO permitas que se establezca a null.
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

    private int calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            // Podrías lanzar una excepción si la fecha de nacimiento es obligatoria
            return 0;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    private NiñoDto convertirADto(Niño niño) {
        Integer idAcudiente = null;
        String nombreAcudiente = null;
        String parentescoAcudiente = null;
        String telefonoAcudiente = null;
        String cedulaAcudiente = null;

        if (niño.getAcudiente() != null) {
            idAcudiente = niño.getAcudiente().getIdAcudiente();
            cedulaAcudiente = niño.getAcudiente().getCedula();
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
                idAcudiente,
                cedulaAcudiente
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
        niño.setIdNiño(dto.getIdNiño()); // Solo si es para actualización
        niño.setNombre(dto.getNombre());
        niño.setnIdentificacion(dto.getnIdentificacion());
        niño.setSexo(dto.getSexo());
        niño.setFechaNacimiento(dto.getFechaNacimiento());
        niño.setFoto(dto.getFoto());
        niño.setEdad(calcularEdad(dto.getFechaNacimiento()));

        // ¡IMPORTANTE!: No intentes asignar el Acudiente aquí en 'convertirADominio'
        // para el caso de creación. Se hará explícitamente en 'crearNiño' después de buscarlo.
        // Si este método se usa para actualizar, la lógica para el acudiente se maneja
        // en 'actualizarNiño'.
        // if (dto.getIdAcudiente() != null) {
        //     acudienteRepository.findById(dto.getIdAcudiente())
        //             .ifPresent(niño::setAcudiente);
        // }

        return niño;
    }

    @Override
    public Map<String, Long> contarNiñosPorGenero() {
        List<Niño> niños = niñoRepository.findAll(); // o una consulta directa si prefieres

        Map<String, Long> conteo = niños.stream()
                .filter(n -> n.getSexo() != null)
                .collect(Collectors.groupingBy(
                        niño -> niño.getSexo().toUpperCase(), // 'M' o 'F'
                        Collectors.counting()
                ));

        // Asegurar que ambos géneros estén presentes aunque no haya ninguno en BD
        conteo.putIfAbsent("MASCULINO", 0L) ; // Asumiendo que guardas "Masculino" y "Femenino" completos
        conteo.putIfAbsent("FEMENINO", 0L);

        return conteo;
    }

}