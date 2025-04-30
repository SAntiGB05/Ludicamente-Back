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

    @Autowired
    private PostService postService;

    @Operation(summary = "Crear un nuevo post")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Post creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada")
    })
    @PostMapping
    public ResponseEntity<Post> crearPost(@RequestBody Post post) {
        Post nuevoPost = postService.crearPost(post);
        return ResponseEntity.status(201).body(nuevoPost);
    }

    @Operation(summary = "Listar todos los posts")
    @ApiResponse(responseCode = "200", description = "Lista de posts obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<Post>> listarPosts() {
        return ResponseEntity.ok(postService.listarPosts());
    }

    @Operation(summary = "Obtener un post por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post encontrado"),
            @ApiResponse(responseCode = "404", description = "Post no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Post> obtenerPostPorId(
            @Parameter(description = "ID del post", example = "1") @PathVariable Integer id) {
        return postService.obtenerPostPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar un post existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Post no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Post> actualizarPost(
            @Parameter(description = "ID del post a actualizar", example = "1") @PathVariable Integer id,
            @RequestBody Post postActualizado) {
        return postService.actualizarPost(id, postActualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un post")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Post eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Post no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPost(
            @Parameter(description = "ID del post a eliminar", example = "1") @PathVariable Integer id) {
        if (postService.eliminarPost(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Buscar posts por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Post>> obtenerPostsPorEstado(@PathVariable Post.EstadoPost estado) {
        return ResponseEntity.ok(postService.buscarPorEstado(estado));
    }

    @Operation(summary = "Buscar posts por título, contenido o etiquetas")
    @GetMapping("/buscar")
    public ResponseEntity<List<Post>> buscarPosts(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String contenido,
            @RequestParam(required = false) String etiquetas) {
        return ResponseEntity.ok(postService.buscarPorTitulo(titulo, contenido, etiquetas));
    }
}