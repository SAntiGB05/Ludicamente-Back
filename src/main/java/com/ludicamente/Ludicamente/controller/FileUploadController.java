package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.service.GcsService; // Importa tu servicio GCS
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files") // Define el prefijo para los endpoints relacionados con archivos
@CrossOrigin(origins = "http://localhost:8080") // ¡IMPORTANTE! Reemplaza esto con la URL real de tu frontend
public class FileUploadController {

    private final GcsService gcsService;

    // Spring inyecta automáticamente el servicio GcsService
    public FileUploadController(GcsService gcsService) {
        this.gcsService = gcsService;
    }

    /**
     * Endpoint para subir una imagen a Google Cloud Storage.
     * Recibe la imagen como un MultipartFile.
     *
     * @param file El archivo de imagen enviado desde el frontend.
     * @return ResponseEntity con la URL pública de la imagen o un mensaje de error.
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("image") MultipartFile file) {
        // Validaciones iniciales del archivo
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha seleccionado ningún archivo para subir.");
        }
        if (!file.getContentType().startsWith("image/")) {
            // Puedes ser más específico aquí, por ejemplo: (image/jpeg|image/png|image/gif)
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Solo se permiten archivos de imagen (JPEG, PNG, GIF, etc.).");
        }
        // Puedes agregar una validación de tamaño de archivo (ej. máximo 5MB)
        long maxFileSize = 5 * 1024 * 1024; // 5 MB
        if (file.getSize() > maxFileSize) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "El archivo es demasiado grande. El tamaño máximo permitido es de 5MB.");
        }

        try {
            // Llama al servicio GcsService para subir el archivo
            String publicUrl = gcsService.uploadFile(file);

            // Prepara la respuesta JSON para el frontend
            Map<String, String> response = new HashMap<>();
            response.put("message", "Imagen subida con éxito.");
            response.put("publicUrl", publicUrl);
            return ResponseEntity.ok(response); // Retorna 200 OK con la URL
        } catch (IOException e) {
            // Loguea el error interno para depuración
            System.err.println("Error al subir archivo a GCS: " + e.getMessage());
            // Retorna un error 500 al frontend
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al subir la imagen. Inténtalo de nuevo.", e);
        } catch (Exception e) {
            // Captura cualquier otra excepción inesperada
            System.err.println("Ocurrió un error inesperado al subir el archivo: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado. Por favor, contacta al soporte.", e);
        }
    }
}