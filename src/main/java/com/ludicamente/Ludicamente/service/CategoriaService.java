package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {

    Categoria crearCategoria(Categoria categoria);
    List<Categoria> listarCategorias();
    Optional<Categoria> actualizarCategoria(Integer id, Categoria categoriaActualizada);
    boolean eliminarCategoria(Integer id);
}