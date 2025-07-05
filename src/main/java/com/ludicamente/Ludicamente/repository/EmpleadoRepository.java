// Archivo: src/main/java/com/ludicamente/Ludicamente/repository/EmpleadoRepository.java
// Descripción: Interfaz del Repositorio para la entidad Empleado.

package com.ludicamente.Ludicamente.repository;

import com.ludicamente.Ludicamente.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    List<Empleado> findByEstado(Empleado.EstadoEmpleado estado);
    Optional<Empleado> findByCorreo(String correoEmpleado); // Asegúrate de tener este método
}
