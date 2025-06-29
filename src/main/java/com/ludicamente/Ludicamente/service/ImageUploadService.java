// src/main/java/com/ludicamente/Ludicamente/service/ImageUploadService.java
package com.ludicamente.Ludicamente.service;

import com.cloudinary.Cloudinary;
import com.ludicamente.Ludicamente.model.GaleriaImagen;
import com.ludicamente.Ludicamente.repository.GaleriaImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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

            GaleriaImagen galeriaImagen = new GaleriaImagen();
            galeriaImagen.setUrlImagen(secureUrl);
            galeriaImagen.setFechaSubida(LocalDateTime.now());
            galeriaImagen.setVisible(true); // Siempre visible al subir

            galeriaImagenRepository.save(galeriaImagen);

            return secureUrl;
        } catch (IOException e) {
            System.err.println("Error al subir imagen a Cloudinary: " + e.getMessage());
            throw new IOException("No se pudo subir la imagen a Cloudinary", e);
        }
    }

    public List<String> getAllImageUrls() {
        // Este método sigue devolviendo solo las imágenes visibles para la galería pública.
        return galeriaImagenRepository.findByVisibleTrue()
                .stream()
                .map(GaleriaImagen::getUrlImagen)
                .toList();
    }

    /**
     * ¡NUEVO MÉTODO!
     * Obtiene las URLs de las imágenes que están marcadas como NO visibles.
     * @return Una lista de URLs de imágenes ocultas.
     */
    public List<String> getHiddenImageUrls() {
        return galeriaImagenRepository.findByVisibleFalse()
                .stream()
                .map(GaleriaImagen::getUrlImagen)
                .toList();
    }

    public boolean hideImage(String imageUrl) {
        Optional<GaleriaImagen> optionalImage = galeriaImagenRepository.findByUrlImagen(imageUrl);
        if (optionalImage.isPresent()) {
            GaleriaImagen imagen = optionalImage.get();
            imagen.setVisible(false); // Cambia el estado a no visible
            galeriaImagenRepository.save(imagen);
            return true;
        }
        return false;
    }

    /**
     * ¡NUEVO MÉTODO!
     * Marca una imagen como visible (la "muestra" de nuevo) por su URL.
     * @param imageUrl La URL de la imagen a mostrar.
     * @return true si la imagen fue encontrada y mostrada, false de lo contrario.
     */
    public boolean showImage(String imageUrl) {
        Optional<GaleriaImagen> optionalImage = galeriaImagenRepository.findByUrlImagen(imageUrl);
        if (optionalImage.isPresent()) {
            GaleriaImagen imagen = optionalImage.get();
            imagen.setVisible(true); // Cambia el estado a visible
            galeriaImagenRepository.save(imagen);
            return true;
        }
        return false; // La imagen no fue encontrada
    }
}