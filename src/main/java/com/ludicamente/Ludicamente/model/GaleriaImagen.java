// src/main/java/com/ludicamente/Ludicamente/model/GaleriaImagen.java
package com.ludicamente.Ludicamente.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "galeria_imagen")
public class GaleriaImagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "url_imagen", nullable = false, unique = true)
    private String urlImagen;

    @Column(name = "fecha_subida", nullable = false)
    private LocalDateTime fechaSubida;

    @Column(name = "visible", nullable = false) // <-- Este campo ya lo tienes
    private Boolean visible;

    public GaleriaImagen() {
        this.visible = true; // Valor por defecto al crear una nueva instancia
    }

    public GaleriaImagen(String urlImagen) {
        this.urlImagen = urlImagen;
        this.fechaSubida = LocalDateTime.now();
        this.visible = true; // Por defecto es visible
    }

    // Getters y Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public Boolean getVisible() { // <-- Getter ya existe
        return visible;
    }

    public void setVisible(Boolean visible) { // <-- Setter ya existe
        this.visible = visible;
    }
}