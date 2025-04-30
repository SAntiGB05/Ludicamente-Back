package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.model.Post;
import com.ludicamente.Ludicamente.repository.PostRepository;
import com.ludicamente.Ludicamente.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    @Operation(summary = "Crear un nuevo post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    public Post crearPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    @Operation(summary = "Listar todos los posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de posts obtenida exitosamente")
    })
    public List<Post> listarPosts() {
        return postRepository.findAll();
    }

    @Override
    @Operation(summary = "Obtener un post por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Post no encontrado")
    })
    public Optional<Post> obtenerPostPorId(
            @Parameter(description = "ID del post a obtener", example = "1") Integer id) {
        return postRepository.findById(id);
    }

    @Override
    @Operation(summary = "Actualizar un post existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Post no encontrado")
    })
    public Optional<Post> actualizarPost(
            @Parameter(description = "ID del post a actualizar", example = "1") Integer id,
            Post postActualizado) {

        Optional<Post> postExistente = postRepository.findById(id);
        if (postExistente.isPresent()) {
            Post post = postExistente.get();
            post.setTitulo(postActualizado.getTitulo());
            post.setContenido(postActualizado.getContenido());
            post.setEstado(postActualizado.getEstado());
            post.setImagenDestacada(postActualizado.getImagenDestacada());
            post.setResumen(postActualizado.getResumen());
            post.setEtiquetas(postActualizado.getEtiquetas());
            post.setVisitas(postActualizado.getVisitas());
            post.setEmpleado(postActualizado.getEmpleado());
            post.setFechaActualizacion(java.time.LocalDateTime.now());
            return Optional.of(postRepository.save(post));
        }
        return Optional.empty();
    }

    @Override
    @Operation(summary = "Eliminar un post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Post no encontrado")
    })
    public boolean eliminarPost(
            @Parameter(description = "ID del post a eliminar", example = "1") Integer id) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Post> buscarPorEstado(Post.EstadoPost estado) {
        return postRepository.findByEstado(estado);
    }

    @Override
    @Operation(summary = "Buscar posts por título, contenido o etiquetas")
    public List<Post> buscarPorTitulo(String titulo, String contenido, String etiquetas) {
        return postRepository.findByTituloContainingIgnoreCaseOrContenidoContainingIgnoreCaseOrEtiquetasContainingIgnoreCase(
                titulo != null ? titulo : "",
                contenido != null ? contenido : "",
                etiquetas != null ? etiquetas : ""
        );
    }


    @Override
    public List<Post> buscarPorIdEmpleado(Integer idEmpleado) {
        return postRepository.findByEmpleado_IdEmpleado(idEmpleado);
    }
}