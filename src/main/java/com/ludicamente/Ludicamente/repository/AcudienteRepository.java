package com.ludicamente.Ludicamente.repository;

import com.ludicamente.Ludicamente.model.Acudiente;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcudienteRepository extends JpaRepository<Acudiente, Integer> {
    Acudiente findByCedula(String cedula);
    Acudiente findByCorreo(String correo);
}