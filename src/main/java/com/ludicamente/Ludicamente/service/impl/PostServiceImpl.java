// Archivo: src/main/java/com/ludicamente/Ludicamente/service/impl/PostServiceImpl.java
// Descripción: Implementación completa del servicio de Posts con manejo de plantillas

package com.ludicamente.Ludicamente.service.impl;

import com.ludicamente.Ludicamente.dto.PostCreateDTO;
import com.ludicamente.Ludicamente.dto.PostUpdateDTO;
import com.ludicamente.Ludicamente.model.Empleado;
import com.ludicamente.Ludicamente.model.Post;
import com.ludicamente.Ludicamente.repository.EmpleadoRepository;
import com.ludicamente.Ludicamente.repository.PostRepository;
import com.ludicamente.Ludicamente.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final EmpleadoRepository empleadoRepository;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, EmpleadoRepository empleadoRepository) {
        this.postRepository = postRepository;
        this.empleadoRepository = empleadoRepository;
    }

    // Método auxiliar para obtener el ID del empleado autenticado
    private Integer getAuthenticatedEmployeeId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Usuario no autenticado.");
        }

        String authenticatedUserEmail = authentication.getName();
        Empleado empleado = empleadoRepository.findByCorreo(authenticatedUserEmail)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado para el usuario autenticado: " + authenticatedUserEmail));
        return empleado.getIdEmpleado();
    }

    // Método auxiliar para verificar permisos de modificación
    private boolean isAuthorizedToModify(Post post) {
        Integer authenticatedEmployeeId = getAuthenticatedEmployeeId();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return isAdmin || (post.getEmpleado() != null && post.getEmpleado().getIdEmpleado().equals(authenticatedEmployeeId));
    }

    @Override
    public Post crearPost(PostCreateDTO postCreateDTO) {
        Integer authenticatedEmployeeId = getAuthenticatedEmployeeId();
        Empleado empleado = empleadoRepository.findById(authenticatedEmployeeId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + authenticatedEmployeeId));

        Post post = new Post();
        post.setTitulo(postCreateDTO.getTitulo());
        post.setContenido(postCreateDTO.getContenido());
        post.setImagenDestacada(postCreateDTO.getImagenDestacada());
        post.setEstado(postCreateDTO.getEstado());
        post.setPlantilla(postCreateDTO.getPlantilla() != null ?
                postCreateDTO.getPlantilla() :
                Post.PlantillaPost.PLANTILLA1);
        post.setEmpleado(empleado);

        // Campos con valores por defecto
        post.setFechaPublicacion(LocalDateTime.now());
        post.setFechaActualizacion(LocalDateTime.now());
        post.setVisitas(0);

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
    public Optional<Post> actualizarPost(Integer id, PostUpdateDTO postUpdateDTO) {
        return postRepository.findById(id).map(post -> {
            if (!isAuthorizedToModify(post)) {
                throw new RuntimeException("No autorizado para modificar este post.");
            }

            // Actualizar campos permitidos
            if (postUpdateDTO.getTitulo() != null) {
                post.setTitulo(postUpdateDTO.getTitulo());
            }
            if (postUpdateDTO.getContenido() != null) {
                post.setContenido(postUpdateDTO.getContenido());
            }
            if (postUpdateDTO.getEstado() != null) {
                post.setEstado(postUpdateDTO.getEstado());
            }
            if (postUpdateDTO.getPlantilla() != null) {
                post.setPlantilla(postUpdateDTO.getPlantilla());
            }
            if (postUpdateDTO.getImagenDestacada() != null) {
                post.setImagenDestacada(postUpdateDTO.getImagenDestacada());
            }
            if (postUpdateDTO.getResumen() != null) {
                post.setResumen(postUpdateDTO.getResumen());
            }
            if (postUpdateDTO.getEtiquetas() != null) {
                post.setEtiquetas(postUpdateDTO.getEtiquetas());
            }
            if (postUpdateDTO.getVisitas() != null) {
                post.setVisitas(postUpdateDTO.getVisitas());
            }

            post.setFechaActualizacion(LocalDateTime.now());
            return postRepository.save(post);
        });
    }

    @Override
    public boolean eliminarPost(Integer id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            if (!isAuthorizedToModify(post)) {
                throw new RuntimeException("No autorizado para eliminar este post.");
            }
            postRepository.delete(post);
            return true;
        }
        return false;
    }

    @Override
    public List<Post> buscarPorEstado(Post.EstadoPost estado) {
        return postRepository.findByEstado(estado);
    }

    @Override
    public List<Post> buscarPorTitulo(String titulo, String contenido, String etiquetas) {
        if ((titulo == null || titulo.trim().isEmpty()) &&
                (contenido == null || contenido.trim().isEmpty()) &&
                (etiquetas == null || etiquetas.trim().isEmpty())) {
            return postRepository.findAll();
        }
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

    @Override
    public Optional<Post> updatePostStatus(Integer postId, Post.EstadoPost newStatus) {
        return postRepository.findById(postId).map(post -> {
            if (!isAuthorizedToModify(post)) {
                throw new RuntimeException("No autorizado para cambiar el estado de este post.");
            }
            post.setEstado(newStatus);
            post.setFechaActualizacion(LocalDateTime.now());
            return postRepository.save(post);
        });
    }

    @Override
    public Optional<Post> cambiarPlantillaPost(Integer postId, Post.PlantillaPost nuevaPlantilla) {
        return postRepository.findById(postId).map(post -> {
            if (!isAuthorizedToModify(post)) {
                throw new RuntimeException("No autorizado para cambiar la plantilla de este post.");
            }
            post.setPlantilla(nuevaPlantilla);
            post.setFechaActualizacion(LocalDateTime.now());
            return postRepository.save(post);
        });
    }

    @Override
    public List<Post> buscarPorPlantilla(Post.PlantillaPost plantilla) {
        return postRepository.findByPlantilla(plantilla);
    }
}