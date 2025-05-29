package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.model.Acudiente;
import com.ludicamente.Ludicamente.model.Empleado;
import com.ludicamente.Ludicamente.repository.EmpleadoRepository;
import com.ludicamente.Ludicamente.service.EmpleadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "Crear un nuevo empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empleado creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    public Empleado crearEmpleado(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }

    // 2. Listar todos los empleados
    @Override
    @Operation(summary = "Obtener todos los empleados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de empleados obtenida exitosamente")
    })
    public List<Empleado> listarEmpleados() {
        return empleadoRepository.findAll();
    }

    // 3. Actualizar un empleado
    @Override
    @Operation(summary = "Actualizar un empleado existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empleado actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    public Optional<Empleado> actualizarEmpleado(
            @Parameter(description = "ID del empleado a actualizar", example = "1") Integer id,
            Empleado empleadoActualizado) {

        Optional<Empleado> empleadoExistente = empleadoRepository.findById(id);
        if (empleadoExistente.isPresent()) {
            Empleado empleado = empleadoExistente.get();
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
    @Operation(summary = "Eliminar un empleado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empleado eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Empleado no encontrado")
    })
    public boolean eliminarEmpleado(@Parameter(description = "ID del empleado a eliminar", example = "1") Integer id) {
        if (empleadoRepository.existsById(id)) {
            empleadoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Empleado obtenerEmpleadoPorCorreo(String correo) {
        return empleadoRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Acudiente no encontrado con correo: " + correo));
    }
}
