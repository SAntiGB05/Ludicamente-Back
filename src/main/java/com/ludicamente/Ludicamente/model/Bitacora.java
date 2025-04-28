package com.ludicamente.Ludicamente.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "bitacora")
public class Bitacora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bitacora")
    private Integer idBitacora;

    @Column(name = "accion", nullable = false, length = 50)
    private String accion;

    @Column(name = "tabla_afectada", nullable = false, length = 50)
    private String tablaAfectada;

    @Column(name = "id_registro_afectado")
    private Integer idRegistroAfectado;

    @Column(name = "datos_anteriores", columnDefinition = "json")
    private String datosAnteriores;

    @Column(name = "datos_nuevos", columnDefinition = "json")
    private String datosNuevos;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "ip_conexion", length = 45)
    private String ipConexion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fkid_empleado")
    private Empleado empleado;

    public Bitacora() {
        this.fechaHora = LocalDateTime.now();
    }
}