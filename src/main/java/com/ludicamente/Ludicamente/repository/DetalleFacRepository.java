package com.ludicamente.Ludicamente.repository;

import com.ludicamente.Ludicamente.model.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleFacRepository extends JpaRepository<DetalleFactura, Integer> {
    // Puedes agregar métodos personalizados aquí si necesitas consultas específicas
}