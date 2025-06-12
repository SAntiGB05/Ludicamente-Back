package com.ludicamente.Ludicamente.service.impl;

// Make sure these imports are correct
import com.ludicamente.Ludicamente.model.Acudiente;
import com.ludicamente.Ludicamente.model.Empleado;
import com.ludicamente.Ludicamente.repository.EmpleadoRepository;
import com.ludicamente.Ludicamente.service.EmpleadoService; // <--- Correct package for EmpleadoService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class EmpleadoServiceImpl implements EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    // 1. Crear un empleado
    @Override
    public Empleado crearEmpleado(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    // 2. Listar todos los empleados
    @Override
    public List<Empleado> listarEmpleados() {
        return empleadoRepository.findAll();
    }

    // 3. Actualizar un empleado
    @Override
    public Optional<Empleado> actualizarEmpleado(Integer id, Empleado empleadoActualizado) {
        Optional<Empleado> empleadoExistente = empleadoRepository.findById(id);
        if (empleadoExistente.isPresent()) {
            Empleado empleado = empleadoExistente.get();
            empleado.setCedulaEmpleado(empleadoActualizado.getCedulaEmpleado());
            empleado.setNombre(empleadoActualizado.getNombre());
            empleado.setCorreo(empleadoActualizado.getCorreo());
            empleado.setTelefono(empleadoActualizado.getTelefono());
            empleado.setDireccion(empleadoActualizado.getDireccion());
            empleado.setSalario(empleadoActualizado.getSalario());
            empleado.setNivelAcceso(empleadoActualizado.getNivelAcceso());
            empleado.setEstado(empleadoActualizado.getEstado());
            empleado.setHorario(empleadoActualizado.getHorario());
            empleado.setFechaContratacion(empleadoActualizado.getFechaContratacion());
            return Optional.of(empleadoRepository.save(empleado));
        }
        return Optional.empty();
    }

    // 4. Eliminar un empleado
    @Override
    public boolean eliminarEmpleado(Integer id) {
        if (empleadoRepository.existsById(id)) {
            empleadoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Empleado obtenerEmpleadoPorCorreo(String correo) {
        return empleadoRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con correo: " + correo));
    }

    @Override
    public Optional<Empleado> obtenerEmpleadoPorId(Integer id) {
        return empleadoRepository.findById(id);
    }
}

