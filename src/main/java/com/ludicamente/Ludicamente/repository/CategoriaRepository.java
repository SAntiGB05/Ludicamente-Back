package com.ludicamente.Ludicamente.repository;

import com.ludicamente.Ludicamente.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{
}

