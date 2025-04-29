package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.model.Post;
import com.ludicamente.Ludicamente.repository.PostRepository;
import com.ludicamente.Ludicamente.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post crearPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> listarPosts() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> obtenerPostPorId(Integer id) {
        return postRepository.findById(id);
    }

    @Override
    public Optional<Post> actualizarPost(Integer id, Post postActualizado) {
        Optional<Post> postExistente = postRepository.findById(id);
        if (postExistente.isPresent()) {
            Post post = postExistente.get();
            post.setTitulo(postActualizado.getTitulo());
            post.setContenido(postActualizado.getContenido());
            post.setEstado(postActualizado.getEstado());
            post.setImagenDestacada(postActualizado.getImagenDestacada());
            post.setResumen(postActualizado.getResumen());
            post.setEtiquetas(postActualizado.getEtiquetas());
            post.setEmpleado(postActualizado.getEmpleado());
            post.setVisitas(postActualizado.getVisitas());
            return Optional.of(postRepository.save(post));
        }
        return Optional.empty();
    }

    @Override
    public boolean eliminarPost(Integer id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Post> obtenerPostsPorEstado(Post.EstadoPost estado) {
        return postRepository.findByEstado(estado);
    }

    @Override
    public List<Post> buscarPosts(String titulo, String contenido, String etiquetas) {
        return postRepository.findByTituloContainingOrContenidoContainingOrEtiquetasContaining(titulo, contenido, etiquetas);
    }
}