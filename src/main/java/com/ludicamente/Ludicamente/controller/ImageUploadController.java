// src/main/java/com/ludicamente/Ludicamente/controller/ImageUploadController.java
package com.ludicamente.Ludicamente.controller; // Ajusta el paquete a tu estructura

import com.ludicamente.Ludicamente.service.ImageUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api") // Puedes ajustar el prefijo de tu API si ya tienes uno
public class ImageUploadController {

    @Autowired
    private ImageUploadService imageUploadService;

    // @CrossOrigin (opcional si ya manejas CORS globalmente con Spring Security o un CorsConfig Bean)
    @PostMapping("/upload/image") // Endpoint específico para subir imágenes
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
            successResponse.put("url", imageUrl); // La URL pública de Cloudinary
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (IOException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error al subir la imagen: " + e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName()); // Tipo de excepción
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            // Captura cualquier otra excepción inesperada
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Ocurrió un error inesperado al procesar la imagen: " + e.getMessage());
            errorResponse.put("error", e.getClass().getSimpleName());
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}