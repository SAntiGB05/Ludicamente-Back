// src/main/java/com/ludicamente/Ludicamente/controller/ImageUploadController.java
package com.ludicamente.Ludicamente.controller;

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
@RequestMapping("/api") // <-- Asegúrate que esta anotación esté aquí
public class ImageUploadController {

    @Autowired
    private ImageUploadService imageUploadService;

    // Endpoint para subir una imagen
    @PostMapping("/upload/image") // <-- Asegúrate que esta anotación esté aquí
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile multipartFile) {
        try {
            String url = imageUploadService.uploadImage(multipartFile);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Imagen subida exitosamente");
            response.put("url", url);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            System.err.println("Error al subir imagen: " + e.getMessage());
            return new ResponseEntity<>("Error al subir imagen: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para obtener imágenes visibles (públicas) de la galería
    @GetMapping("/gallery/images") // <-- ¡Este es el que te está dando 404!
    public ResponseEntity<List<String>> getGalleryImages() {
        List<String> imageUrls = imageUploadService.getAllImageUrls();
        return new ResponseEntity<>(imageUrls, HttpStatus.OK);
    }

    // Endpoint para obtener imágenes ocultas de la galería
    @GetMapping("/gallery/hidden-images") // <-- ¡Y este también!
    public ResponseEntity<List<String>> getHiddenGalleryImages() {
        List<String> imageUrls = imageUploadService.getHiddenImageUrls();
        return new ResponseEntity<>(imageUrls, HttpStatus.OK);
    }

    // Endpoint para obtener TODAS las imágenes directamente de Cloudinary
    @GetMapping("/gallery/all-cloudinary-images")
    public ResponseEntity<List<String>> getAllCloudinaryImages() {
        try {
            List<String> imageUrls = imageUploadService.getAllImagesFromCloudinary();
            return new ResponseEntity<>(imageUrls, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error al obtener TODAS las imágenes de Cloudinary: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para ocultar una imagen
    @PutMapping("/gallery/hide-image")
    public ResponseEntity<String> hideImage(@RequestBody Map<String, String> payload) {
        String imageUrl = payload.get("imageUrl");
        if (imageUrl == null || imageUrl.isEmpty()) {
            return new ResponseEntity<>("URL de imagen no proporcionada.", HttpStatus.BAD_REQUEST);
        }
        boolean success = imageUploadService.hideImage(imageUrl);
        if (success) {
            return new ResponseEntity<>("Imagen oculta exitosamente.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Imagen no encontrada o no se pudo ocultar.", HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para mostrar una imagen
    @PutMapping("/gallery/show-image")
    public ResponseEntity<String> showImage(@RequestBody Map<String, String> payload) {
        String imageUrl = payload.get("imageUrl");
        if (imageUrl == null || imageUrl.isEmpty()) {
            return new ResponseEntity<>("URL de imagen no proporcionada.", HttpStatus.BAD_REQUEST);
        }
        boolean success = imageUploadService.showImage(imageUrl);
        if (success) {
            return new ResponseEntity<>("Imagen mostrada exitosamente.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Imagen no encontrada o no se pudo mostrar.", HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para importar una imagen de Cloudinary a la base de datos local
    @PostMapping("/gallery/import-image")
    public ResponseEntity<String> importImage(@RequestBody Map<String, String> payload) {
        String imageUrl = payload.get("imageUrl");
        if (imageUrl == null || imageUrl.isEmpty()) {
            return new ResponseEntity<>("URL de imagen no proporcionada.", HttpStatus.BAD_REQUEST);
        }
        try {
            imageUploadService.importImageFromCloudinary(imageUrl); // Llama al método del servicio
            return new ResponseEntity<>("Imagen importada y registrada en la base de datos con éxito: " + imageUrl, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error al importar imagen: " + e.getMessage());
            return new ResponseEntity<>("Error al importar imagen: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}