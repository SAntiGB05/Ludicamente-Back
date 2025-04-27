package com.ludicamente.Ludicamente.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public abstract class NiñoDao implements JpaRepository<NiñoDao, Long> {
}
