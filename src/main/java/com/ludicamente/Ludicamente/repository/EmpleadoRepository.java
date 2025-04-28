package com.ludicamente.Ludicamente.repository;

import com.ludicamente.Ludicamente.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {
    List<Empleado> findByEstado(String estado);
}