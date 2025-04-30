package com.ludicamente.Ludicamente.repository;

import com.ludicamente.Ludicamente.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    // Buscar posts por estado
    List<Post> findByEstado(Post.EstadoPost estado);

    List<Post> findByTituloContainingIgnoreCaseOrContenidoContainingIgnoreCaseOrEtiquetasContainingIgnoreCase(
            String titulo, String contenido, String etiquetas);

    // Buscar posts por empleado
    List<Post> findByEmpleado_IdEmpleado(Integer idEmpleado);
}