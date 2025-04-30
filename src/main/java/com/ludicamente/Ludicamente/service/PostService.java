package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Post crearPost(Post post);
    List<Post> listarPosts();
    Optional<Post> obtenerPostPorId(Integer id);
    Optional<Post> actualizarPost(Integer id, Post postActualizado);
    boolean eliminarPost(Integer id);
    List<Post> buscarPorEstado(Post.EstadoPost estado);
    List<Post> buscarPorTitulo(String titulo, String contenido, String etiquetas);
    List<Post> buscarPorIdEmpleado(Integer idEmpleado);
}