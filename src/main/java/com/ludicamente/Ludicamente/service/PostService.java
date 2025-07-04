package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.dto.PostCreateDTO;
import com.ludicamente.Ludicamente.dto.PostUpdateDTO;
import com.ludicamente.Ludicamente.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Post crearPost(PostCreateDTO postCreateDTO);
    List<Post> listarPosts();
    Optional<Post> obtenerPostPorId(Integer id);
    Optional<Post> actualizarPost(Integer id, PostUpdateDTO postUpdateDTO);
    boolean eliminarPost(Integer id);
    List<Post> buscarPorEstado(Post.EstadoPost estado);
    List<Post> buscarPorTitulo(String titulo, String contenido, String etiquetas);
    List<Post> buscarPorIdEmpleado(Integer idEmpleado);
    Optional<Post> updatePostStatus(Integer postId, Post.EstadoPost newStatus);

    // Nuevo método para cambiar plantilla
    Optional<Post> cambiarPlantillaPost(Integer postId, Post.PlantillaPost nuevaPlantilla);

    // Nuevo método para buscar por plantilla
    List<Post> buscarPorPlantilla(Post.PlantillaPost plantilla);
}