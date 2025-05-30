package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.config.JwtService;
import com.ludicamente.Ludicamente.model.Acudiente;
import com.ludicamente.Ludicamente.model.Niño;
import com.ludicamente.Ludicamente.repository.AcudienteRepository;

import com.ludicamente.Ludicamente.repository.EmpleadoRepository;
import com.ludicamente.Ludicamente.repository.NiñoRepository;
import jakarta.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.ludicamente.Ludicamente.auth.RegisterAcudienteRequest;
import com.ludicamente.Ludicamente.service.AcudienteService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AcudienteServiceImpl implements AcudienteService {

    private final EmpleadoRepository empleadoRepository;
    private final AcudienteRepository acudienteRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired

    public AcudienteServiceImpl(EmpleadoRepository empleadoRepository, AcudienteRepository acudienteRepository, NiñoRepository niñoRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.empleadoRepository = empleadoRepository;
        this.acudienteRepository = acudienteRepository;
        this.passwordEncoder = passwordEncoder;
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
        acudiente.setContraseña(acudienteDetails.getContraseña());
        acudiente.setTelefono(acudienteDetails.getTelefono());
        acudiente.setParentesco(acudienteDetails.getParentesco());
        acudiente.setDireccion(acudienteDetails.getDireccion());

        return acudienteRepository.save(acudiente);
    }

    @Override
    public void eliminarAcudiente(Integer id) {
        acudienteRepository.deleteById(id);
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
        return acudienteRepository.save(acudiente); // Guardar acudiente y niños por cascade
    }
}