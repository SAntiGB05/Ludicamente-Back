package com.ludicamente.Ludicamente.repository;

import com.ludicamente.Ludicamente.model.Bitacora;
import com.ludicamente.Ludicamente.model.Niño;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BitacoraRepository extends JpaRepository<Bitacora, Integer> {
    List<Bitacora> findByNiñoAndEstadoTrue(Niño niño);
    List<Bitacora> findByNiño(Niño niño);
    Optional<Bitacora> findByNiñoAndCodBitacora(Niño niño, Integer codBitacora);


}