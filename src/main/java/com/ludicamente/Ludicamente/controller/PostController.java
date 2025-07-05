package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.dto.PostCreateDTO;
import com.ludicamente.Ludicamente.dto.PostResponseDTO;
import com.ludicamente.Ludicamente.dto.PostUpdateDTO;
import com.ludicamente.Ludicamente.model.Post;
import com.ludicamente.Ludicamente.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // Método para mapear Post a PostResponseDTO
    private PostResponseDTO mapToDto(Post post) {
        PostResponseDTO dto = new PostResponseDTO();
        dto.setIdPost(post.getIdPost());
        dto.setTitulo(post.getTitulo());
        dto.setContenido(post.getContenido());
        dto.setFechaPublicacion(post.getFechaPublicacion());
        dto.setFechaActualizacion(post.getFechaActualizacion());
        dto.setEstado(post.getEstado());
        dto.setImagenDestacada(post.getImagenDestacada());
        dto.setResumen(post.getResumen());
        dto.setEtiquetas(post.getEtiquetas());
        dto.setVisitas(post.getVisitas());

        if (post.getEmpleado() != null) {
            dto.setFkidEmpleado(post.getEmpleado().getIdEmpleado());
        } else {
            dto.setFkidEmpleado(null);
        }

        dto.setPlantilla(post.getPlantilla());
        return dto;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')") // <--- CAMBIO AQUÍ
    public ResponseEntity<PostResponseDTO> createPost(@RequestBody PostCreateDTO postCreateDTO) {
        Post createdPost = postService.crearPost(postCreateDTO);
        return new ResponseEntity<>(mapToDto(createdPost), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<Post> posts = postService.listarPosts();
        List<PostResponseDTO> dtos = posts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Integer id) {
        return postService.obtenerPostPorId(id)
                .map(this::mapToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')") // <--- CAMBIO AQUÍ
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Integer id, @RequestBody PostUpdateDTO postUpdateDTO) {
        return postService.actualizarPost(id, postUpdateDTO)
                .map(this::mapToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')") // <--- CAMBIO AQUÍ
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        boolean deleted = postService.eliminarPost(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{postId}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')") // <--- CAMBIO AQUÍ
    public ResponseEntity<PostResponseDTO> updatePostStatus(@PathVariable Integer postId, @RequestParam Post.EstadoPost newStatus) {
        return postService.updatePostStatus(postId, newStatus)
                .map(this::mapToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{postId}/plantilla")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')") // <--- CAMBIO AQUÍ
    public ResponseEntity<PostResponseDTO> updatePostTemplate(@PathVariable Integer postId, @RequestParam Post.PlantillaPost nuevaPlantilla) {
        return postService.cambiarPlantillaPost(postId, nuevaPlantilla)
                .map(this::mapToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/plantilla/{plantilla}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByPlantilla(@PathVariable Post.PlantillaPost plantilla) {
        List<Post> posts = postService.buscarPorPlantilla(plantilla);
        List<PostResponseDTO> dtos = posts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByEstado(@PathVariable Post.EstadoPost estado) {
        List<Post> posts = postService.buscarPorEstado(estado);
        List<PostResponseDTO> dtos = posts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<PostResponseDTO>> searchPosts(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String contenido,
            @RequestParam(required = false) String etiquetas) {
        List<Post> posts = postService.buscarPorTitulo(titulo, contenido, etiquetas);
        List<PostResponseDTO> dtos = posts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/empleado/{idEmpleado}")
    public ResponseEntity<List<PostResponseDTO>> getPostsByEmployeeId(@PathVariable Integer idEmpleado) {
        List<Post> posts = postService.buscarPorIdEmpleado(idEmpleado);
        List<PostResponseDTO> dtos = posts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}