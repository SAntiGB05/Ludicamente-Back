package com.ludicamente.Ludicamente.repository;

import com.ludicamente.Ludicamente.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    // Método para obtener todos los posts publicados
    List<Post> findByEstado(Post.EstadoPost estado);

    // Método para buscar posts por título, contenido o etiquetas (búsqueda FullText)
    List<Post> findByTituloContainingOrContenidoContainingOrEtiquetasContaining(String titulo, String contenido, String etiquetas);

    // Método para obtener un post por su ID
    Optional<Post> findById(Integer id);

    // Puedes agregar más métodos personalizados según tus necesidades
}