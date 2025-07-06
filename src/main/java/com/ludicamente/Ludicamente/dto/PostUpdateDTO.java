
package com.ludicamente.Ludicamente.dto;

import com.ludicamente.Ludicamente.model.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min; // Importación para @Min

public class PostUpdateDTO {

    private Post.PlantillaPost plantilla;

    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 100, message = "El título no puede exceder los 100 caracteres")
    private String titulo;

    @NotBlank(message = "El contenido no puede estar vacío")
    private String contenido;

    private String imagenDestacada; // Opcional

    @NotNull(message = "El estado no puede ser nulo")
    private Post.EstadoPost estado; // Usamos el enum del modelo

    @Size(max = 255, message = "El resumen no puede exceder los 255 caracteres")
    private String resumen;

    @Size(max = 255, message = "Las etiquetas no pueden exceder los 255 caracteres")
    private String etiquetas;

    @Min(value = 0, message = "Las visitas no pueden ser negativas")
    private Integer visitas;

    // Constructor vacío
    public PostUpdateDTO() {}

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

    public Post.EstadoPost getEstado() {
        return estado;
    }

    public void setEstado(Post.EstadoPost estado) {
        this.estado = estado;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(String etiquetas) {
        this.etiquetas = etiquetas;
    }

    public Integer getVisitas() {
        return visitas;
    }

    public void setVisitas(Integer visitas) {
        this.visitas = visitas;
    }
}