// src/main/java/com/ludicamente/Ludicamente/controller/ImageUploadController.java
package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.model.GaleriaImagen; // Necesario si usas el endpoint de admin
import com.ludicamente.Ludicamente.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ImageUploadController {

    @Autowired
    private ImageUploadService imageUploadService;

    @PostMapping("/upload/image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("image") MultipartFile file) {
        if (file.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "No se ha seleccionado ningún archivo para subir.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            String imageUrl = imageUploadService.uploadImage(file);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Imagen subida exitosamente.");
            successResponse.put("url", imageUrl);
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (IOException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error al subir la imagen: " + e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Ocurrió un error inesperado al procesar la imagen: " + e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/gallery/images")
    public ResponseEntity<List<String>> getGalleryImages() {
        try {
            List<String> imageUrls = imageUploadService.getAllImageUrls();
            return new ResponseEntity<>(imageUrls, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error al obtener imágenes de la galería: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para ocultar una imagen
    @PutMapping("/gallery/hide-image")
    public ResponseEntity<Map<String, String>> hideImage(@RequestBody Map<String, String> payload) {
        String imageUrl = payload.get("imageUrl");
        if (imageUrl == null || imageUrl.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "La URL de la imagen es requerida.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            boolean hidden = imageUploadService.hideImage(imageUrl);
            if (hidden) {
                Map<String, String> successResponse = new HashMap<>();
                successResponse.put("message", "Imagen oculta exitosamente.");
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            } else {
                Map<String, String> notFoundResponse = new HashMap<>();
                notFoundResponse.put("message", "Imagen no encontrada con la URL proporcionada.");
                return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.err.println("Error al ocultar imagen: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Ocurrió un error al intentar ocultar la imagen: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * ¡NUEVO ENDPOINT!
     * Devuelve solo las URLs de las imágenes que están marcadas como NO visibles.
     */
    @GetMapping("/gallery/hidden-images")
    public ResponseEntity<List<String>> getHiddenGalleryImages() {
        try {
            List<String> imageUrls = imageUploadService.getHiddenImageUrls();
            return new ResponseEntity<>(imageUrls, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error al obtener imágenes ocultas de la galería: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * ¡NUEVO ENDPOINT!
     * Marca una imagen como visible (la "muestra" de nuevo) por su URL.
     */
    @PutMapping("/gallery/show-image")
    public ResponseEntity<Map<String, String>> showImage(@RequestBody Map<String, String> payload) {
        String imageUrl = payload.get("imageUrl");
        if (imageUrl == null || imageUrl.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "La URL de la imagen es requerida.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            boolean shown = imageUploadService.showImage(imageUrl);
            if (shown) {
                Map<String, String> successResponse = new HashMap<>();
                successResponse.put("message", "Imagen mostrada exitosamente.");
                return new ResponseEntity<>(successResponse, HttpStatus.OK);
            } else {
                Map<String, String> notFoundResponse = new HashMap<>();
                notFoundResponse.put("message", "Imagen no encontrada con la URL proporcionada.");
                return new ResponseEntity<>(notFoundResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            System.err.println("Error al mostrar imagen: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Ocurrió un error al intentar mostrar la imagen: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}