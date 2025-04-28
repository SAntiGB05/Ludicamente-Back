package com.ludicamente.Ludicamente.model;
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
    private EstadoPost estado = EstadoPost.BORRADOR;

    @Column(name = "imagen_destacada", length = 255)
    private String imagenDestacada;

    @Column(name = "resumen", length = 255)
    private String resumen;

    @Column(name = "etiquetas", length = 255)
    private String etiquetas;

    @Column(name = "visitas")
    private Integer visitas = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_empleado", nullable = false)
    private Empleado empleado;

    public enum EstadoPost {
        BORRADOR,
        PUBLICADO,
        ARCHIVADO
    }

    public Post() {
        this.fechaPublicacion = LocalDateTime.now();
    }
}