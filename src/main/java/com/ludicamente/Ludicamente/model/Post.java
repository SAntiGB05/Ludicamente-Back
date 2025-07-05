package com.ludicamente.Ludicamente.model;

import com.fasterxml.jackson.annotation.JsonBackReference; // <-- Cambiar a JsonBackReference
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_post")
    private Integer idPost;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "contenido", nullable = false, columnDefinition = "LONGTEXT")
    private String contenido;

    @Column(name = "fecha_publicacion", nullable = false)
    private LocalDateTime fechaPublicacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "estado", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPost estado = EstadoPost.BORRADOR; // Valor por defecto

    @Column(name = "imagen_destacada", length = 255)
    private String imagenDestacada;

    @Column(name = "resumen", length = 255)
    private String resumen;

    @Column(name = "etiquetas", length = 255)
    private String etiquetas;

    @Column(name = "visitas")
    private Integer visitas = 0; // Valor por defecto

    @Column(name = "plantilla", nullable = false)
    @Enumerated(EnumType.STRING)
    private PlantillaPost plantilla = PlantillaPost.PLANTILLA1; // Valor por defecto

    // Relación Many-to-One con la entidad Empleado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_empleado", nullable = false) // Columna de clave foránea
    @JsonBackReference("empleado-posts") // <-- CORREGIDO: Debe ser JsonBackReference
    private Empleado empleado;


    public enum EstadoPost {
        BORRADOR,
        PUBLICADO,
        ARCHIVADO
    }

    public enum PlantillaPost {
        PLANTILLA1,
        PLANTILLA2,
        PLANTILLA3,
        PLANTILLA4
    }

    // Constructor vacío. Se inicializa fechaPublicacion aquí.
    public Post() {
        this.fechaPublicacion = LocalDateTime.now();
    }

    // Getters y Setters

    public PlantillaPost getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(PlantillaPost plantilla) {
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

    public EstadoPost getEstado() {
        return estado;
    }

    public void setEstado(EstadoPost estado) {
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

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
}