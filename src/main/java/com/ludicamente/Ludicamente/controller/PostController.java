package com.ludicamente.Ludicamente.controller;
import com.ludicamente.Ludicamente.model.Post;
import com.ludicamente.Ludicamente.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Crear un nuevo post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    @PostMapping
    public ResponseEntity<Post> crearPost(@RequestBody Post post) {
        Post nuevoPost = postService.crearPost(post);
        return ResponseEntity.status(201).body(nuevoPost);
    }

    @Operation(summary = "Obtener todos los posts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de posts obtenida exitosamente")
    })
    @GetMapping
    public ResponseEntity<List<Post>> listarPosts() {
        List<Post> posts = postService.listarPosts();
        return ResponseEntity.ok(posts);
    }

    @Operation(summary = "Obtener un post por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Post no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Post> obtenerPostPorId(
            @Parameter(description = "ID del post a obtener", example = "1")
            @PathVariable Integer id) {
        Optional<Post> post = postService.obtenerPostPorId(id);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar un post existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Post actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Post no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Post> actualizarPost(
            @Parameter(description = "ID del post a actualizar", example = "1")
            @PathVariable Integer id,
            @RequestBody Post postActualizado) {
        Optional<Post> post = postService.actualizarPost(id, postActualizado);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un post")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Post no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPost(
            @Parameter(description = "ID del post a eliminar", example = "1")
            @PathVariable Integer id) {
        if (postService.eliminarPost(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}