// Archivo: src/main/java/com/ludicamente/Ludicamente/repository/PostRepository.java
// Descripción: Interfaz del Repositorio para la entidad Post.

package com.ludicamente.Ludicamente.repository;

import com.ludicamente.Ludicamente.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    // Buscar posts por estado
    List<Post> findByEstado(Post.EstadoPost estado);

    // Buscar posts por título, contenido o etiquetas (ignorando mayúsculas/minúsculas)
    List<Post> findByTituloContainingIgnoreCaseOrContenidoContainingIgnoreCaseOrEtiquetasContainingIgnoreCase(
            String titulo, String contenido, String etiquetas);

    // Buscar posts por el ID del empleado asociado
    List<Post> findByEmpleado_IdEmpleado(Integer idEmpleado); // **Cambiado a Integer**

    List<Post> findByPlantilla(Post.PlantillaPost plantilla);
}
