package com.ludicamente.Ludicamente.dto;

import java.time.LocalDateTime;

public class PostDto {

    private Integer idPost;
    private String titulo;
    private String contenido;
    private LocalDateTime fechaPublicacion;
    private LocalDateTime fechaActualizacion;
    private String estado; // Puede ser 'borrador', 'publicado' o 'archivado'
    private String imagenDestacada;
    private Integer fkidEmpleado;

    // Constructor vacío
    public PostDto() {}

    // Constructor con parámetros
    public PostDto(Integer idPost, String titulo, String contenido,
                   LocalDateTime fechaPublicacion, LocalDateTime fechaActualizacion,
                   String estado, String imagenDestacada, Integer fkidEmpleado) {
        this.idPost = idPost;
        this.titulo = titulo;
        this.contenido = contenido;
        this.fechaPublicacion = fechaPublicacion;
        this.fechaActualizacion = fechaActualizacion;
        this.estado = estado;
        this.imagenDestacada = imagenDestacada;
        this.fkidEmpleado = fkidEmpleado;
    }

    // Getters y Setters
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getImagenDestacada() {
        return imagenDestacada;
    }

    public void setImagenDestacada(String imagenDestacada) {
        this.imagenDestacada = imagenDestacada;
    }

    public Integer getFkidEmpleado() {
        return fkidEmpleado;
    }

    public void setFkidEmpleado(Integer fkidEmpleado) {
        this.fkidEmpleado = fkidEmpleado;
    }
}