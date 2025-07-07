package com.ludicamente.Ludicamente.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator; // <-- ¡NUEVA IMPORTACIÓN!
import com.fasterxml.jackson.annotation.JsonValue;   // <-- ¡NUEVA IMPORTACIÓN!
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

    // Asegúrate de que @Enumerated(EnumType.STRING) esté presente, lo cual ya tienes.
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

    @Column(name = "plantilla", nullable = false)
    @Enumerated(EnumType.STRING)
    private PlantillaPost plantilla = PlantillaPost.PLANTILLA1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_empleado", nullable = false)
    @JsonBackReference("empleado-posts")
    private Empleado empleado;

    // --- MODIFICACIÓN DE ENUM EstadoPost ---
    public enum EstadoPost {
        BORRADOR,
        PUBLICADO, // Los nombres de las constantes siguen en MAYÚSCULAS en Java
        ARCHIVADO;

        // Método para que Jackson sepa cómo convertir una cadena (ej. "publicado")
        // a una constante del enum (ej. PUBLICADO).
        @JsonCreator
        public static EstadoPost fromString(String value) {
            for (EstadoPost estado : EstadoPost.values()) {
                // Comparamos ignorando mayúsculas/minúsculas
                if (estado.name().equalsIgnoreCase(value)) {
                    return estado;
                }
            }
            throw new IllegalArgumentException("Valor de EstadoPost inválido: " + value);
        }

        // Método para que Jackson sepa cómo convertir una constante del enum (ej. PUBLICADO)
        // a una cadena para el JSON de salida (ej. "publicado").
        @JsonValue
        public String toValue() {
            // Convertimos el nombre de la constante a minúsculas para que coincida con la DB
            return this.name().toLowerCase();
        }
    }

    // --- MODIFICACIÓN DE ENUM PlantillaPost (opcional, pero buena práctica si tu DB usa minúsculas) ---
    public enum PlantillaPost {
        PLANTILLA1,
        PLANTILLA2,
        PLANTILLA3,
        PLANTILLA4;

        @JsonCreator
        public static PlantillaPost fromString(String value) {
            for (PlantillaPost plantilla : PlantillaPost.values()) {
                if (plantilla.name().equalsIgnoreCase(value)) {
                    return plantilla;
                }
            }
            throw new IllegalArgumentException("Valor de PlantillaPost inválido: " + value);
        }

        @JsonValue
        public String toValue() {
            // Si tu base de datos espera "PLANTILLA1", déjalo como this.name();
            // Si espera "plantilla1", cámbialo a this.name().toLowerCase();
            return this.name();
        }
    }

    public Post() {
        this.fechaPublicacion = LocalDateTime.now();
    }

    // ... Resto de Getters y Setters (ya los tienes correctamente) ...

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