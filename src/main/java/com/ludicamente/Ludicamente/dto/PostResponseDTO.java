// Archivo: src/main/java/com/ludicamente/Ludicamente/dto/PostResponseDTO.java
// Descripción: DTO para enviar la representación de un Post desde el backend al frontend.
// Contiene todos los campos relevantes de un Post, incluyendo el ID y las fechas.

package com.ludicamente.Ludicamente.dto;

import com.ludicamente.Ludicamente.model.Post;
import java.time.LocalDateTime;

public class PostResponseDTO {

    private Post.PlantillaPost plantilla;
    private Integer idPost;
    private String titulo;
    private String contenido;
    private LocalDateTime fechaPublicacion;
    private LocalDateTime fechaActualizacion;
    private Post.EstadoPost estado; // Usamos el enum del modelo
    private String imagenDestacada;
    private String resumen;
    private String etiquetas;
    private Integer visitas;
    private Integer fkidEmpleado; // **Cambiado a Integer** para coincidir con Empleado.idEmpleado

    // Constructor vacío
    public PostResponseDTO() {}



    // Constructor para mapear desde una entidad Post
    public PostResponseDTO(Post post) {
        this.plantilla = post.getPlantilla();
        this.idPost = post.getIdPost();
        this.titulo = post.getTitulo();
        this.contenido = post.getContenido();
        this.fechaPublicacion = post.getFechaPublicacion();
        this.fechaActualizacion = post.getFechaActualizacion();
        this.estado = post.getEstado();
        this.imagenDestacada = post.getImagenDestacada();
        this.resumen = post.getResumen();
        this.etiquetas = post.getEtiquetas();
        this.visitas = post.getVisitas();
        if (post.getEmpleado() != null) {
            this.fkidEmpleado = post.getEmpleado().getIdEmpleado();
        }
    }

    // Getters y Setters
    public Post.PlantillaPost getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(Post.PlantillaPost plantilla) {
        this.plantilla = plantilla;
    }

    public Integer getIdPost() {
        return idPost;
    }

    public void setIdPost(Integer idPost) {
        this.idPost = idPost;
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

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Post.EstadoPost getEstado() {
        return estado;
    }

    public void setEstado(Post.EstadoPost estado) {
        this.estado = estado;
    }

    public String getImagenDestacada() {
        return imagenDestacada;
    }

    public void setImagenDestacada(String imagenDestacada) {
        this.imagenDestacada = imagenDestacada;
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

    public Integer getFkidEmpleado() {
        return fkidEmpleado;
    }

    public void setFkidEmpleado(Integer fkidEmpleado) {
        this.fkidEmpleado = fkidEmpleado;
    }
}
