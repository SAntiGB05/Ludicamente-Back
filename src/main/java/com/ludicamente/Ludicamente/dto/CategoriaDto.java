package com.ludicamente.Ludicamente.dto;

public class CategoriaDto {
    private Integer codCategoria;
    private String nombreCategoria;

    public CategoriaDto(Integer codCategoria, String nombreCategoria){
        this.codCategoria = codCategoria;
        this.nombreCategoria = nombreCategoria;
    }

    public Integer getCodCategoria() {
        return codCategoria;
    }

    public void setCodCategoria(Integer codCategoria) {
        this.codCategoria = codCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}
