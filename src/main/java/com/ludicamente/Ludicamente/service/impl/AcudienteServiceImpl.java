package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.config.JwtService;
import com.ludicamente.Ludicamente.dto.AcudienteDto;
import com.ludicamente.Ludicamente.model.Acudiente;
import com.ludicamente.Ludicamente.model.Niño;
import com.ludicamente.Ludicamente.repository.AcudienteRepository;
import com.ludicamente.Ludicamente.repository.EmpleadoRepository;
import com.ludicamente.Ludicamente.repository.NiñoRepository; // Mantener si es necesario para otros métodos, de lo contrario, eliminar
import jakarta.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager; // Mantener si es necesario, de lo contrario, eliminar
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.ludicamente.Ludicamente.auth.RegisterAcudienteRequest;
import com.ludicamente.Ludicamente.service.AcudienteService;
import org.springframework.beans.factory.annotation.Autowired; // @Autowired en campos se puede eliminar con inyección por constructor

@Service
public class AcudienteServiceImpl implements AcudienteService {

    // Se eliminó @Autowired de las declaraciones de campo ya que la inyección por constructor es preferida
    private final EmpleadoRepository empleadoRepository;
    private final AcudienteRepository acudienteRepository;
    private final PasswordEncoder passwordEncoder;

    // Opcional: Si NiñoRepository, JwtService y AuthenticationManager NO se usan directamente
    // dentro de este servicio, puedes eliminarlos del constructor para un código más limpio.
    // Sin embargo, si otras partes de tu aplicación dependen implícitamente de que se inyecten aquí,
    // asegúrate de que se manejen correctamente. Por ahora, mostraré cómo incluirlos y asignarlos
    // si *debes* mantenerlos en el constructor.
    private final NiñoRepository niñoRepository; // Declarado si es necesario
    private final JwtService jwtService; // Declarado si es necesario
    private final AuthenticationManager authenticationManager; // Declarado si es necesario


    // Inyección de constructor preferida. No es necesario @Autowired en el constructor si es el único.
    public AcudienteServiceImpl(
            EmpleadoRepository empleadoRepository,
            AcudienteRepository acudienteRepository,
            NiñoRepository niñoRepository, // Mantener si NiñoRepository se usa realmente en este servicio
            PasswordEncoder passwordEncoder,
            JwtService jwtService, // Mantener si JwtService se usa realmente en este servicio
            AuthenticationManager authenticationManager // Mantener si AuthenticationManager se usa realmente en este servicio
    ) {
        this.empleadoRepository = empleadoRepository;
        this.acudienteRepository = acudienteRepository;
        this.passwordEncoder = passwordEncoder;
        // Asignar estos si están presentes en el constructor y se usan en esta clase
        this.niñoRepository = niñoRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public List<Acudiente> listarAcudientes() {
        return acudienteRepository.findAll();
    }

    public Acudiente obtenerAcudientePorCedula(String cedula) {
        return acudienteRepository.findByCedula(cedula)
                .orElseThrow(() -> new RuntimeException("Acudiente no encontrado con cédula: " + cedula));
    }

    @Override
    public Acudiente obtenerAcudientePorCorreo(String correo) {
        return acudienteRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Acudiente no encontrado con correo: " + correo));
    }

    @Override
    public Acudiente guardarAcudiente(Acudiente acudiente) {
        return acudienteRepository.save(acudiente);
    }

    @Override
    public Acudiente actualizarAcudiente(Integer id, Acudiente acudienteDetails) {
        Acudiente acudiente = acudienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Acudiente no encontrado con id: " + id));

        acudiente.setNombre(acudienteDetails.getNombre());
        acudiente.setCedula(acudienteDetails.getCedula());
        acudiente.setCorreo(acudienteDetails.getCorreo());
        // CORRECCIÓN DE SEGURIDAD: Solo actualiza y codifica la contraseña si se proporciona
        if (acudienteDetails.getContraseña() != null && !acudienteDetails.getContraseña().isEmpty()) {
            acudiente.setContraseña(passwordEncoder.encode(acudienteDetails.getContraseña()));
        }
        acudiente.setTelefono(acudienteDetails.getTelefono());
        acudiente.setParentesco(acudienteDetails.getParentesco());
        acudiente.setDireccion(acudienteDetails.getDireccion());

        return acudienteRepository.save(acudiente);
    }

    @Override
    @Transactional
    public Acudiente actualizarAcudienteAdmin(Integer id, AcudienteDto acudienteDetails) {
        // Verificar si el acudiente existe
        Acudiente acudiente = acudienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Acudiente no encontrado con ID: " + id));

        // Mapear los datos del DTO a la entidad
        acudiente.setCedula(acudienteDetails.getCedula());
        acudiente.setNombre(acudienteDetails.getNombre());
        acudiente.setCorreo(acudienteDetails.getCorreo());
        acudiente.setTelefono(acudienteDetails.getTelefono());
        acudiente.setParentesco(acudienteDetails.getParentesco());
        // Si AcudienteDto para actualizaciones de administrador incluye la contraseña, codifícala
        if (acudienteDetails.getContraseña() != null && !acudienteDetails.getContraseña().isEmpty()) {
            acudiente.setContraseña(passwordEncoder.encode(acudienteDetails.getContraseña()));
        }
        // Si AcudienteDto para actualizaciones de administrador incluye la dirección
        if (acudienteDetails.getDireccion() != null && !acudienteDetails.getDireccion().isEmpty()){
            acudiente.setDireccion(acudienteDetails.getDireccion());
        }

        // Guardar los cambios
        return acudienteRepository.save(acudiente);
    }

    @Override
    public void eliminarAcudiente(Integer id) {
        acudienteRepository.deleteById(id);
    }

    @Override
    public AcudienteDto obtenerAcudienteAutenticado(String correo) {
        Acudiente acudiente = acudienteRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Acudiente no encontrado"));

        // MODIFICADO: Usando el constructor de 8 argumentos y pasando null para contraseña y dirección.
        // Esto evita modificar AcudienteDto.
        return new AcudienteDto(
                acudiente.getIdAcudiente(),
                acudiente.getCedula(),
                acudiente.getNombre(),
                acudiente.getCorreo(),
                null, // Pasar null o una cadena vacía para contraseña (buenas prácticas de seguridad para respuestas GET)
                acudiente.getTelefono(),
                acudiente.getParentesco(),
                null  // Pasar null o una cadena vacía para dirección si no es necesaria aquí
        );
    }

    @Override
    @Transactional
    public Acudiente registrarAcudienteConNiños(RegisterAcudienteRequest request) {
        // Verificar si el correo ya está registrado
        if (acudienteRepository.findByCorreo(request.getCorreoAcudiente()).isPresent() ||
                empleadoRepository.findByCorreo(request.getCorreoAcudiente()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado por otro usuario.");
        }

        // Crear el acudiente
        Acudiente acudiente = Acudiente.builder()
                .cedula(request.getCedulaAcudiente())
                .nombre(request.getNombreAcudiente())
                .correo(request.getCorreoAcudiente())
                .contraseña(passwordEncoder.encode(request.getContraseñaAcudiente()))
                .telefono(request.getTelefonoAcudiente())
                .parentesco(request.getParentesco())
                .build();

        // Validar que haya al menos un niño
        if (request.getNiños() == null || request.getNiños().isEmpty()) {
            throw new IllegalArgumentException("Debe registrar al menos un niño con el acudiente.");
        }

        // Convertir los DTO de niños a entidades
        List<Niño> niños = request.getNiños().stream().map(dto -> {
            Niño n = new Niño();
            n.setNombre(dto.getNombre());
            n.setnIdentificacion(dto.getnIdentificacion());
            n.setFechaNacimiento(dto.getFechaNacimiento());
            n.setSexo(dto.getSexo());
            n.setEdad(dto.getEdad());
            n.setAcudiente(acudiente); // Relación bidireccional
            return n;
        }).collect(Collectors.toList());

        acudiente.setNiños(niños);
        return acudienteRepository.save(acudiente); // Guardar acudiente y niños por cascada
    }

    public long contarAcudientes() {
        return acudienteRepository.count();
    }
}