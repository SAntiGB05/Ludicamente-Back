// Archivo: src/main/java/com/ludicamente/Ludicamente/dto/PostCreateDTO.java
// Descripción: DTO para recibir los datos necesarios para crear un nuevo Post desde el frontend.
// Se ha eliminado fkidEmpleado, ya que se obtendrá del usuario autenticado en el backend.

package com.ludicamente.Ludicamente.dto;

import com.ludicamente.Ludicamente.model.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostCreateDTO {

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 100, message = "El título no puede exceder los 100 caracteres")
    private String titulo;

    @NotNull(message = "La plantilla no puede ser nula")
    private Post.PlantillaPost plantilla = Post.PlantillaPost.PLANTILLA1; // Valor por defecto

    @NotBlank(message = "El contenido no puede estar vacío")
    private String contenido;

    private String imagenDestacada; // Opcional

    // Se ha eliminado fkidEmpleado de este DTO.
    // El ID del empleado se obtendrá del contexto de seguridad en el servicio.

    @NotNull(message = "El estado no puede ser nulo")
    private Post.EstadoPost estado; // Usamos el enum directamente para el estado

    // Constructor vacío
    public PostCreateDTO() {}

    // Getters y Setters

    public Post.PlantillaPost getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(Post.PlantillaPost plantilla) {
        this.plantilla = plantilla;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getImagenDestacada() {
        return imagenDestacada;
    }

    public void setImagenDestacada(String imagenDestacada) {
        this.imagenDestacada = imagenDestacada;
    }

    // Ya no necesitamos un getter/setter para fkidEmpleado aquí
    // public Integer getFkidEmpleado() { return fkidEmpleado; }
    // public void setFkidEmpleado(Integer fkidEmpleado) { this.fkidEmpleado = fkidEmpleado; }

    public Post.EstadoPost getEstado() {
        return estado;
    }

    public void setEstado(Post.EstadoPost estado) {
        this.estado = estado;
    }
}
