package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.model.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoService {

    // Crear un empleado
    Empleado crearEmpleado(Empleado empleado);

    // Listar todos los empleados
    List<Empleado> listarEmpleados();

    // Actualizar un empleado
    Optional<Empleado> actualizarEmpleado(Integer id, Empleado empleadoActualizado);

    // Eliminar un empleado
    boolean eliminarEmpleado(Integer id);
}
