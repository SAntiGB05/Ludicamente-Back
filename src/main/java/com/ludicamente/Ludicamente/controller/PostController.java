// Archivo: src/main/java/com/ludicamente/Ludicamente/controller/PostController.java
// Descripción: Controlador REST actualizado para manejar operaciones de Post incluyendo plantillas

package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.dto.PostCreateDTO;
import com.ludicamente.Ludicamente.dto.PostResponseDTO;
import com.ludicamente.Ludicamente.dto.PostUpdateDTO;
import com.ludicamente.Ludicamente.model.Post;
import com.ludicamente.Ludicamente.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {

    @Autowired
    private PostService postService;

    @Operation(summary = "Crear un nuevo post")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Post creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada o empleado no encontrado"),
            @ApiResponse(responseCode = "401", description = "No autorizado: token JWT ausente o inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<PostResponseDTO> crearPost(@Valid @RequestBody PostCreateDTO postCreateDTO) {
        try {
            Post nuevoPost = postService.crearPost(postCreateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new PostResponseDTO(nuevoPost));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Listar todos los posts")
    @ApiResponse(responseCode = "200", description = "Lista de posts obtenida exitosamente")
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> listarPosts() {
        List<Post> posts = postService.listarPosts();
        List<PostResponseDTO> postDTOs = posts.stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postDTOs);
    }

    @Operation(summary = "Obtener un post por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post encontrado"),
            @ApiResponse(responseCode = "404", description = "Post no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> obtenerPostPorId(
            @Parameter(description = "ID del post", example = "1") @PathVariable Integer id) {
        return postService.obtenerPostPorId(id)
                .map(post -> ResponseEntity.ok(new PostResponseDTO(post)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar un post existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Post actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en los datos de entrada"),
            @ApiResponse(responseCode = "403", description = "No autorizado para modificar este post"),
            @ApiResponse(responseCode = "404", description = "Post no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> actualizarPost(
            @Parameter(description = "ID del post a actualizar", example = "1") @PathVariable Integer id,
            @Valid @RequestBody PostUpdateDTO postUpdateDTO) {
        try {
            return postService.actualizarPost(id, postUpdateDTO)
                    .map(post -> ResponseEntity.ok(new PostResponseDTO(post)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("No autorizado")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Eliminar un post")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Post eliminado exitosamente"),
            @ApiResponse(responseCode = "403", description = "No autorizado para eliminar este post"),
            @ApiResponse(responseCode = "404", description = "Post no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPost(
            @Parameter(description = "ID del post a eliminar", example = "1") @PathVariable Integer id) {
        try {
            if (postService.eliminarPost(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("No autorizado")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Actualizar el estado de un post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado del post actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Estado inválido o error en la solicitud"),
            @ApiResponse(responseCode = "403", description = "No autorizado para cambiar el estado de este post"),
            @ApiResponse(responseCode = "404", description = "Post no encontrado")
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PostResponseDTO> updatePostStatus(
            @Parameter(description = "ID del post", example = "1") @PathVariable Integer id,
            @Parameter(description = "Nuevo estado del post (BORRADOR, PUBLICADO, ARCHIVADO)", example = "ARCHIVADO")
            @RequestParam Post.EstadoPost newStatus) {
        try {
            return postService.updatePostStatus(id, newStatus)
                    .map(post -> ResponseEntity.ok(new PostResponseDTO(post)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("No autorizado")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Cambiar la plantilla de un post")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Plantilla del post actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Plantilla inválida o error en la solicitud"),
            @ApiResponse(responseCode = "403", description = "No autorizado para cambiar la plantilla de este post"),
            @ApiResponse(responseCode = "404", description = "Post no encontrado")
    })
    @PatchMapping("/{id}/plantilla")
    public ResponseEntity<PostResponseDTO> cambiarPlantillaPost(
            @Parameter(description = "ID del post", example = "1") @PathVariable Integer id,
            @Parameter(description = "Nueva plantilla del post (PLANTILLA1, PLANTILLA2, PLANTILLA3, PLANTILLA4)", example = "PLANTILLA2")
            @RequestParam Post.PlantillaPost nuevaPlantilla) {
        try {
            return postService.cambiarPlantillaPost(id, nuevaPlantilla)
                    .map(post -> ResponseEntity.ok(new PostResponseDTO(post)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            if (e.getMessage().contains("No autorizado")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Buscar posts por plantilla")
    @ApiResponse(responseCode = "200", description = "Lista de posts obtenida exitosamente")
    @GetMapping("/plantilla/{plantilla}")
    public ResponseEntity<List<PostResponseDTO>> buscarPorPlantilla(
            @Parameter(description = "Tipo de plantilla", example = "PLANTILLA1")
            @PathVariable Post.PlantillaPost plantilla) {
        List<Post> posts = postService.buscarPorPlantilla(plantilla);
        List<PostResponseDTO> postDTOs = posts.stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postDTOs);
    }

    @Operation(summary = "Buscar posts por estado")
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<PostResponseDTO>> obtenerPostsPorEstado(
            @PathVariable Post.EstadoPost estado) {
        List<Post> posts = postService.buscarPorEstado(estado);
        List<PostResponseDTO> postDTOs = posts.stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postDTOs);
    }

    @Operation(summary = "Buscar posts por título, contenido o etiquetas")
    @GetMapping("/buscar")
    public ResponseEntity<List<PostResponseDTO>> buscarPosts(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) String contenido,
            @RequestParam(required = false) String etiquetas) {
        List<Post> posts = postService.buscarPorTitulo(titulo, contenido, etiquetas);
        List<PostResponseDTO> postDTOs = posts.stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postDTOs);
    }

    @Operation(summary = "Obtener posts por ID de empleado")
    @GetMapping("/empleado/{idEmpleado}")
    public ResponseEntity<List<PostResponseDTO>> obtenerPostsPorIdEmpleado(
            @Parameter(description = "ID del empleado", example = "1") @PathVariable Integer idEmpleado) {
        List<Post> posts = postService.buscarPorIdEmpleado(idEmpleado);
        List<PostResponseDTO> postDTOs = posts.stream()
                .map(PostResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(postDTOs);
    }
}