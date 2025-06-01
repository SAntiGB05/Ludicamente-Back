package com.ludicamente.Ludicamente.repository;

import com.ludicamente.Ludicamente.model.Niño;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NiñoRepository extends JpaRepository<Niño, Integer> {
    List<Niño> findByAcudienteIdAcudiente(Integer idAcudiente);
}