package com.ludicamente.Ludicamente.repository;

import com.ludicamente.Ludicamente.model.Acudiente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AcudienteRepository extends JpaRepository<Acudiente, Integer> {
    Optional<Acudiente> findByCorreo(String correo);
    Optional<Acudiente> findByCedula(String cedula);
}