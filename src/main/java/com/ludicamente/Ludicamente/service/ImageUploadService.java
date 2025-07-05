// src/main/java/com/ludicamente/Ludicamente/service/ImageUploadService.java
package com.ludicamente.Ludicamente.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Search; // Importa la clase Search
import com.cloudinary.api.ApiResponse; // Importa ApiResponse
import com.ludicamente.Ludicamente.model.GaleriaImagen;
import com.ludicamente.Ludicamente.repository.GaleriaImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList; // Necesario para construir la lista de URLs
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ImageUploadService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private GaleriaImagenRepository galeriaImagenRepository;

    public String uploadImage(MultipartFile multipartFile) throws IOException {
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("folder", "ludicamente_uploads");
            options.put("resource_type", "auto");

            Map uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), options);
            String secureUrl = (String) uploadResult.get("secure_url");

            // Opcional: Si quieres registrar TODAS las imágenes en tu DB, incluso las que ya existían antes
            // Puedes buscar si ya existe para evitar duplicados en tu DB si la subida fue manual o externa.
            Optional<GaleriaImagen> existingImage = galeriaImagenRepository.findByUrlImagen(secureUrl);
            if (!existingImage.isPresent()) {
                GaleriaImagen galeriaImagen = new GaleriaImagen();
                galeriaImagen.setUrlImagen(secureUrl);
                galeriaImagen.setFechaSubida(LocalDateTime.now());
                galeriaImagen.setVisible(true); // Siempre visible al subir
                galeriaImagenRepository.save(galeriaImagen);
            }

            return secureUrl;
        } catch (IOException e) {
            System.err.println("Error al subir imagen a Cloudinary: " + e.getMessage());
            throw new IOException("No se pudo subir la imagen a Cloudinary", e);
        }
    }

    // Añade esto a ImageUploadService.java
    public GaleriaImagen importImageFromCloudinary(String imageUrl) {
        // Primero, verifica si ya existe en tu DB para evitar duplicados
        Optional<GaleriaImagen> existingImage = galeriaImagenRepository.findByUrlImagen(imageUrl);
        if (existingImage.isPresent()) {
            System.out.println("La imagen ya existe en la base de datos: " + imageUrl);
            return existingImage.get(); // Retorna la imagen existente
        }

        GaleriaImagen newImage = new GaleriaImagen();
        newImage.setUrlImagen(imageUrl);
        newImage.setFechaSubida(LocalDateTime.now()); // O puedes poner un valor por defecto o nulo
        newImage.setVisible(true); // Por defecto, se importa como visible
        return galeriaImagenRepository.save(newImage);
    }

    public List<String> getAllImageUrls() {
        return galeriaImagenRepository.findByVisibleTrue()
                .stream()
                .map(GaleriaImagen::getUrlImagen)
                .toList();
    }

    public List<String> getHiddenImageUrls() {
        return galeriaImagenRepository.findByVisibleFalse()
                .stream()
                .map(GaleriaImagen::getUrlImagen)
                .toList();
    }

    // --- ¡NUEVO MÉTODO! Para obtener todas las imágenes directamente de Cloudinary ---
    public List<String> getAllImagesFromCloudinary() throws Exception {
        List<String> imageUrls = new ArrayList<>();
        try {
            // Usa el API de administración de Cloudinary para buscar recursos
            // `expression("resource_type:image")` filtra solo imágenes
            // `max_results` es el número de resultados por página, máximo 500 por defecto
            ApiResponse result = cloudinary.search()
                    .expression("resource_type:image")
                    .maxResults(500) // Puedes ajustar este número o implementar paginación
                    .execute();

            List<Map<String, Object>> resources = (List<Map<String, Object>>) result.get("resources");

            if (resources != null) {
                for (Map<String, Object> resource : resources) {
                    imageUrls.add((String) resource.get("secure_url"));
                }
            }
        } catch (Exception e) {
            System.err.println("Error al listar imágenes de Cloudinary: " + e.getMessage());
            throw new Exception("No se pudieron obtener las imágenes de Cloudinary: " + e.getMessage(), e);
        }
        return imageUrls;
    }

    public boolean hideImage(String imageUrl) {
        Optional<GaleriaImagen> optionalImage = galeriaImagenRepository.findByUrlImagen(imageUrl);
        if (optionalImage.isPresent()) {
            GaleriaImagen imagen = optionalImage.get();
            imagen.setVisible(false);
            galeriaImagenRepository.save(imagen);
            return true;
        }
        return false;
    }

    public boolean showImage(String imageUrl) {
        Optional<GaleriaImagen> optionalImage = galeriaImagenRepository.findByUrlImagen(imageUrl);
        if (optionalImage.isPresent()) {
            GaleriaImagen imagen = optionalImage.get();
            imagen.setVisible(true);
            galeriaImagenRepository.save(imagen);
            return true;
        }
        return false;
    }
}