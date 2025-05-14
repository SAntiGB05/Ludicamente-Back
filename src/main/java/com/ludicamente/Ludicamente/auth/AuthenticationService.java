package com.ludicamente.Ludicamente.auth;

import com.ludicamente.Ludicamente.auth.userdetails.AcudienteUserDetails;
import com.ludicamente.Ludicamente.auth.userdetails.EmpleadoUserDetails;
import com.ludicamente.Ludicamente.model.Acudiente;
import com.ludicamente.Ludicamente.model.Empleado;
import com.ludicamente.Ludicamente.model.Niño;
import com.ludicamente.Ludicamente.repository.AcudienteRepository;
import com.ludicamente.Ludicamente.repository.EmpleadoRepository;
import com.ludicamente.Ludicamente.repository.NiñoRepository;
import com.ludicamente.Ludicamente.config.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {

    private final EmpleadoRepository empleadoRepository;
    private final AcudienteRepository acudienteRepository;
    private final NiñoRepository niñoRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(EmpleadoRepository empleadoRepository,
                                 AcudienteRepository acudienteRepository,
                                 NiñoRepository niñoRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager) {
        this.empleadoRepository = empleadoRepository;
        this.acudienteRepository = acudienteRepository;
        this.niñoRepository = niñoRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    // Registro de Empleado
    @Transactional
    public AuthResponse registerEmpleado(RegisterEmpleadoRequest request) {
        if (empleadoRepository.findByCorreo(request.getCorreoEmpleado()).isPresent() ||
                acudienteRepository.findByCorreo(request.getCorreoEmpleado()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado por otro usuario.");
        }

        var empleado = Empleado.builder()
                .nombre(request.getNombreEmpleado())
                .correo(request.getCorreoEmpleado())
                .contraseña(passwordEncoder.encode(request.getContraseñaEmpleado()))
                .cedula(request.getCedulaEmpleado())
                .nivelAcceso(request.getNivelAcceso())
                .telefono(request.getTelefonoEmpleado())
                .direccion(request.getDireccionEmpleado())
                .fechaContratacion(request.getFechaContartacion())
                .salario(request.getSalario())
                .horario(request.getHorario())
                .build();

        empleadoRepository.save(empleado);
        var empleadoUserDetails = new EmpleadoUserDetails(empleado);
        var jwtToken = jwtService.generateToken(empleadoUserDetails);
        return new AuthResponse(jwtToken);
    }

    // Registro de Acudiente + mínimo un Niño
    @Transactional
    public AuthResponse registerAcudiente(RegisterAcudienteRequest request) {
        if (acudienteRepository.findByCorreo(request.getCorreoAcudiente()).isPresent() ||
                empleadoRepository.findByCorreo(request.getCorreoAcudiente()).isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado por otro usuario.");
        }

        Acudiente acudiente = Acudiente.builder()
                .nombre(request.getNombreAcudiente())
                .correo(request.getCorreoAcudiente())
                .contraseña(passwordEncoder.encode(request.getContraseñaAcudiente()))
                .telefono(request.getTelefonoAcudiente())
                .direccion(request.getDireccionAcudiente())
                .build();

        // Validar que haya al menos un niño
        if (request.getNiños() == null || request.getNiños().isEmpty()) {
            throw new IllegalArgumentException("Debe registrar al menos un niño con el acudiente.");
        }

        List<Niño> niños = request.getNiños().stream().map(dto -> {
            Niño n = new Niño();
            n.setNombre(dto.getNombre());
            n.setFechaNacimiento(dto.getFechaNacimiento());
            n.setSexo(dto.getSexo());
            n.setAcudiente(acudiente); // Relación bidireccional
            return n;
        }).collect(Collectors.toList());

        acudiente.setNiños(niños);
        acudienteRepository.save(acudiente); // Por cascade guarda también los niños

        var acudienteUserDetails = new AcudienteUserDetails(acudiente);
        var jwtToken = jwtService.generateToken(acudienteUserDetails);
        return new AuthResponse(jwtToken);
    }

    public AuthResponse authenticate(AuthRequest request) {
        // 1. Autenticar credenciales
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Buscar como Empleado
        Optional<Empleado> empleadoOpt = empleadoRepository.findByCorreo(request.getEmail());
        if (empleadoOpt.isPresent()) {
            EmpleadoUserDetails empleadoUserDetails = new EmpleadoUserDetails(empleadoOpt.get());
            return new AuthResponse(jwtService.generateToken(empleadoUserDetails));
        }

        // 3. Buscar como Acudiente
        Optional<Acudiente> acudienteOpt = acudienteRepository.findByCorreo(request.getEmail());
        if (acudienteOpt.isPresent()) {
            AcudienteUserDetails acudienteUserDetails = new AcudienteUserDetails(acudienteOpt.get());
            return new AuthResponse(jwtService.generateToken(acudienteUserDetails));
        }

        // 4. Si no existe en ninguna tabla
        throw new UsernameNotFoundException("Usuario no encontrado");
    }
}