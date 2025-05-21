package com.ludicamente.Ludicamente.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bitacora")
public class Bitacora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_bitacora")
    private Integer codBitacora;

    @Column(name = "descripcion_general", columnDefinition = "TEXT")
    private String descripcionGeneral;

    @Column(columnDefinition = "TEXT")
    private String oportunidades;

    @Column(columnDefinition = "TEXT")
    private String debilidades;

    @Column(columnDefinition = "TEXT")
    private String amenazas;

    @Column(columnDefinition = "TEXT")
    private String fortalezas;

    @Column(columnDefinition = "TEXT")
    private String objetivos;

    @Column(columnDefinition = "TEXT")
    private String habilidades;

    @Column(columnDefinition = "TEXT")
    private String seguimiento;

    @Column(name = "historial_actividad", columnDefinition = "TEXT")
    private String historialActividad;

    @ManyToOne
    @JoinColumn(name = "fkid_empleado", referencedColumnName = "id_empleado")
    private Empleado empleado;

    // Getters y Setters

    public Integer getCodBitacora() {
        return codBitacora;
    }

    public void setCodBitacora(Integer codBitacora) {
        this.codBitacora = codBitacora;
    }

    public String getDescripcionGeneral() {
        return descripcionGeneral;
    }

    public void setDescripcionGeneral(String descripcionGeneral) {
        this.descripcionGeneral = descripcionGeneral;
    }

    public String getOportunidades() {
        return oportunidades;
    }

    public void setOportunidades(String oportunidades) {
        this.oportunidades = oportunidades;
    }

    public String getDebilidades() {
        return debilidades;
    }

    public void setDebilidades(String debilidades) {
        this.debilidades = debilidades;
    }

    public String getAmenazas() {
        return amenazas;
    }

    public void setAmenazas(String amenazas) {
        this.amenazas = amenazas;
    }

    public String getFortalezas() {
        return fortalezas;
    }

    public void setFortalezas(String fortalezas) {
        this.fortalezas = fortalezas;
    }

    public String getObjetivos() {
        return objetivos;
    }

    public void setObjetivos(String objetivos) {
        this.objetivos = objetivos;
    }

    public String getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(String habilidades) {
        this.habilidades = habilidades;
    }

    public String getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(String seguimiento) {
        this.seguimiento = seguimiento;
    }

    public String getHistorialActividad() {
        return historialActividad;
    }

    public void setHistorialActividad(String historialActividad) {
        this.historialActividad = historialActividad;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
}
