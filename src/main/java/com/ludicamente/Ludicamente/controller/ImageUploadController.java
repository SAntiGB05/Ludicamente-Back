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
@RequestMapping("/api")
public class ImageUploadController {

    @Autowired
    private ImageUploadService imageUploadService;

    // Endpoint original para subir una imagen (mantener por si lo usas en otro lugar)
    @PostMapping("/upload/image")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile multipartFile) {
        try {
            // Este método ahora llamará a la versión sobrecargada con el folder por defecto
            String url = imageUploadService.uploadImage(multipartFile, "ludicamente_uploads"); // Default folder
            Map<String, String> response = new HashMap<>();
            response.put("message", "Imagen subida exitosamente");
            response.put("url", url);
            System.out.println("Imagen subida a folder por defecto: " + url); // Depuración
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            System.err.println("Error al subir imagen (default folder): " + e.getMessage()); // Depuración
            return new ResponseEntity<>("Error al subir imagen: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // --- NUEVO ENDPOINT PARA SUBIR IMAGEN A UN FOLDER ESPECÍFICO ---
    @PostMapping("/upload/image-to-folder")
    public ResponseEntity<?> uploadImageToFolder(
            @RequestParam("image") MultipartFile multipartFile,
            @RequestParam("folderName") String folderName) { // <--- CORRECCIÓN CLAVE AQUÍ: "folderName"
        try {
            System.out.println("Recibiendo solicitud de subida para folder: " + folderName); // Depuración
            // Llama al nuevo método sobrecargado en el servicio
            String url = imageUploadService.uploadImage(multipartFile, folderName);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Imagen subida exitosamente al folder: " + folderName);
            response.put("url", url);
            System.out.println("Imagen subida exitosamente a Cloudinary en folder '" + folderName + "': " + url); // Depuración
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            System.err.println("Error en ImageUploadController al subir imagen al folder '" + folderName + "': " + e.getMessage()); // Depuración
            return new ResponseEntity<>("Error al subir imagen: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para obtener imágenes visibles (públicas) de la galería
    @GetMapping("/gallery/images")
    public ResponseEntity<List<String>> getGalleryImages() {
        List<String> imageUrls = imageUploadService.getAllImageUrls();
        System.out.println("Obteniendo URLs de imágenes visibles. Cantidad: " + imageUrls.size()); // Depuración
        return new ResponseEntity<>(imageUrls, HttpStatus.OK);
    }

    // Endpoint para obtener imágenes ocultas de la galería
    @GetMapping("/gallery/hidden-images")
    public ResponseEntity<List<String>> getHiddenGalleryImages() {
        List<String> imageUrls = imageUploadService.getHiddenImageUrls();
        System.out.println("Obteniendo URLs de imágenes ocultas. Cantidad: " + imageUrls.size()); // Depuración
        return new ResponseEntity<>(imageUrls, HttpStatus.OK);
    }

    // Endpoint para obtener TODAS las imágenes directamente de Cloudinary
    @GetMapping("/gallery/all-cloudinary-images")
    public ResponseEntity<List<String>> getAllCloudinaryImages() {
        try {
            List<String> imageUrls = imageUploadService.getAllImagesFromCloudinary();
            System.out.println("Obteniendo TODAS las URLs de imágenes de Cloudinary. Cantidad: " + imageUrls.size()); // Depuración
            return new ResponseEntity<>(imageUrls, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error al obtener TODAS las imágenes de Cloudinary desde el controlador: " + e.getMessage()); // Depuración
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para ocultar una imagen
    @PutMapping("/gallery/hide-image")
    public ResponseEntity<String> hideImage(@RequestBody Map<String, String> payload) {
        String imageUrl = payload.get("imageUrl");
        if (imageUrl == null || imageUrl.isEmpty()) {
            System.err.println("URL de imagen no proporcionada para ocultar."); // Depuración
            return new ResponseEntity<>("URL de imagen no proporcionada.", HttpStatus.BAD_REQUEST);
        }
        boolean success = imageUploadService.hideImage(imageUrl);
        if (success) {
            System.out.println("Imagen oculta exitosamente: " + imageUrl); // Depuración
            return new ResponseEntity<>("Imagen oculta exitosamente.", HttpStatus.OK);
        } else {
            System.err.println("Imagen no encontrada o no se pudo ocultar: " + imageUrl); // Depuración
            return new ResponseEntity<>("Imagen no encontrada o no se pudo ocultar.", HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para mostrar una imagen
    @PutMapping("/gallery/show-image")
    public ResponseEntity<String> showImage(@RequestBody Map<String, String> payload) {
        String imageUrl = payload.get("imageUrl");
        if (imageUrl == null || imageUrl.isEmpty()) {
            System.err.println("URL de imagen no proporcionada para mostrar."); // Depuración
            return new ResponseEntity<>("URL de imagen no proporcionada.", HttpStatus.BAD_REQUEST);
        }
        boolean success = imageUploadService.showImage(imageUrl);
        if (success) {
            System.out.println("Imagen mostrada exitosamente: " + imageUrl); // Depuración
            return new ResponseEntity<>("Imagen mostrada exitosamente.", HttpStatus.OK);
        } else {
            System.err.println("Imagen no encontrada o no se pudo mostrar: " + imageUrl); // Depuración
            return new ResponseEntity<>("Imagen no encontrada o no se pudo mostrar.", HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para importar una imagen de Cloudinary a la base de datos local
    @PostMapping("/gallery/import-image")
    public ResponseEntity<String> importImage(@RequestBody Map<String, String> payload) {
        String imageUrl = payload.get("imageUrl");
        if (imageUrl == null || imageUrl.isEmpty()) {
            System.err.println("URL de imagen no proporcionada para importar."); // Depuración
            return new ResponseEntity<>("URL de imagen no proporcionada.", HttpStatus.BAD_REQUEST);
        }
        try {
            imageUploadService.importImageFromCloudinary(imageUrl);
            System.out.println("Imagen importada y registrada en la base de datos con éxito: " + imageUrl); // Depuración
            return new ResponseEntity<>("Imagen importada y registrada en la base de datos con éxito: " + imageUrl, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("Error al importar imagen desde el controlador: " + e.getMessage()); // Depuración
            return new ResponseEntity<>("Error al importar imagen: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}