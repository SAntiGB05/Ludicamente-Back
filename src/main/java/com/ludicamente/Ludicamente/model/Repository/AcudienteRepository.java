package com.ludicamente.Ludicamente.model.Repository;

import com.ludicamente.Ludicamente.model.Acudiente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AcudienteRepository extends JpaRepository<Acudiente, Long> {
    Acudiente findByCedula(String cedula);
    Acudiente findByCorreo(String correo);
}