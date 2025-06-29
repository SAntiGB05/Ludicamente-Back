// src/main/java/com/ludicamente/Ludicamente/repository/GaleriaImagenRepository.java
package com.ludicamente.Ludicamente.repository;

import com.ludicamente.Ludicamente.model.GaleriaImagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional; // Importa Optional para un manejo más seguro

@Repository
public interface GaleriaImagenRepository extends JpaRepository<GaleriaImagen, Integer> {
    // Método para obtener solo las imágenes visibles (ya lo tienes)
    List<GaleriaImagen> findByVisibleTrue();

    // NUEVO: Método para obtener solo las imágenes ocultas
    List<GaleriaImagen> findByVisibleFalse();

    // Método para encontrar por URL de imagen (ya lo tienes, pero con Optional es mejor)
    Optional<GaleriaImagen> findByUrlImagen(String urlImagen);
}