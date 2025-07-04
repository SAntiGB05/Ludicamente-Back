package com.ludicamente.Ludicamente.repository;

import com.ludicamente.Ludicamente.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    Optional<Factura> findById(Integer id);

}
