package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {

    // Crear un nuevo post
    Post crearPost(Post post);

    // Listar todos los posts
    List<Post> listarPosts();

    // Obtener un post por su ID
    Optional<Post> obtenerPostPorId(Integer id);

    // Actualizar un post existente
    Optional<Post> actualizarPost(Integer id, Post postActualizado);

    // Eliminar un post
    boolean eliminarPost(Integer id);

    // Obtener todos los posts de un estado específico (por ejemplo, 'publicado')
    List<Post> obtenerPostsPorEstado(Post.EstadoPost estado);

    // Realizar búsqueda de posts por título, contenido o etiquetas
    List<Post> buscarPosts(String titulo, String contenido, String etiquetas);
}