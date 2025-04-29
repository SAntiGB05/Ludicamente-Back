package com.ludicamente.Ludicamente.repository;

import com.ludicamente.Ludicamente.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    List<Factura> findByEmpleado_IdEmpleado(Integer idEmpleado);
    List<Factura> findByNiño_IdNiño(Integer idNiño);
}