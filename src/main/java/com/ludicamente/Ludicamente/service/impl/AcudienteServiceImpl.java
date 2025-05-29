package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.model.Acudiente;
import com.ludicamente.Ludicamente.repository.AcudienteRepository;
import com.ludicamente.Ludicamente.service.AcudienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AcudienteServiceImpl implements AcudienteService {

    @Autowired
    private AcudienteRepository acudienteRepository;

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
}