package com.ludicamente.Ludicamente.dto;

import java.time.LocalDate;

public class BitacoraDto {

    private String descripcionGeneral;
    private String oportunidades;
    private String debilidades;
    private String amenazas;
    private String fortalezas;
    private String objetivos;
    private String habilidades;
    private String seguimiento;
    private String historialActividad;
    private Integer idEmpleado; // ID del empleado relacionado
    private Boolean estado;
    private Integer idNiño;
    private Integer codBitacora;
    private  String titulo;
    private LocalDate fechaCreacion;

    public BitacoraDto() {}

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }



    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }



    public Integer getCodBitacora() {
        return codBitacora;
    }

    public void setCodBitacora(Integer codBitacora) {
        this.codBitacora = codBitacora;
    }

    public Integer getIdNiño() {
        return idNiño;
    }

    public void setIdNiño(Integer idNiño) {
        this.idNiño = idNiño;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
}
