package com.ludicamente.Ludicamente.repository;

import com.ludicamente.Ludicamente.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicioRepository extends JpaRepository<Servicio, Integer> {
    // Puedes agregar consultas personalizadas aquí si lo necesitas
    List<Servicio> findByCategoriaCodCategoria(Integer codCategoria);

}