package com.ludicamente.Ludicamente.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Search; // Asegúrate de que esta importación esté presente
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.ludicamente.Ludicamente.model.GaleriaImagen;
import com.ludicamente.Ludicamente.repository.GaleriaImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    // Sobrecarga 1: Método original, ahora con un folder por defecto
    public String uploadImage(MultipartFile multipartFile) throws IOException {
        // Por defecto, sube a ludicamente_uploads si no se especifica una carpeta
        System.out.println("Subiendo imagen a folder por defecto: ludicamente_uploads"); // Depuración
        return uploadImage(multipartFile, "ludicamente_uploads");
    }

    // Sobrecarga 2: Nuevo método para subir imagen a un folder específico
    public String uploadImage(MultipartFile multipartFile, String folderName) throws IOException {
        try {
            Map<String, Object> options = new HashMap<>();
            options.put("folder", folderName); // La clave es "folder" para especificar la carpeta de subida
            options.put("resource_type", "auto"); // Cloudinary detectará el tipo de recurso
            options.put("unique_filename", false); // Puedes ajustar esto a 'true' si quieres nombres únicos generados por Cloudinary

            System.out.println("Preparando subida a Cloudinary en folder: " + folderName); // Depuración
            Map uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), options);
            String secureUrl = (String) uploadResult.get("secure_url");
            System.out.println("Imagen subida a Cloudinary. URL: " + secureUrl); // Depuración

            // Opcional: Registrar la URL en tu base de datos local si no existe
            Optional<GaleriaImagen> existingImage = galeriaImagenRepository.findByUrlImagen(secureUrl);
            if (!existingImage.isPresent()) {
                GaleriaImagen galeriaImagen = new GaleriaImagen();
                galeriaImagen.setUrlImagen(secureUrl);
                galeriaImagen.setFechaSubida(LocalDateTime.now());
                galeriaImagen.setVisible(true); // Siempre visible al subir
                galeriaImagenRepository.save(galeriaImagen);
                System.out.println("Imagen registrada en la base de datos."); // Depuración
            } else {
                System.out.println("La imagen ya existe en la base de datos: " + secureUrl); // Depuración
            }

            return secureUrl;
        } catch (IOException e) {
            System.err.println("Error al subir imagen a Cloudinary en folder '" + folderName + "': " + e.getMessage()); // Depuración
            throw new IOException("No se pudo subir la imagen a Cloudinary", e);
        }
    }

    // --- Métodos de la Galería (Restaurados) ---

    public GaleriaImagen importImageFromCloudinary(String imageUrl) {
        Optional<GaleriaImagen> existingImage = galeriaImagenRepository.findByUrlImagen(imageUrl);
        if (existingImage.isPresent()) {
            System.out.println("La imagen ya existe en la base de datos: " + imageUrl); // Depuración
            return existingImage.get();
        }

        GaleriaImagen newImage = new GaleriaImagen();
        newImage.setUrlImagen(imageUrl);
        newImage.setFechaSubida(LocalDateTime.now());
        newImage.setVisible(true);
        GaleriaImagen savedImage = galeriaImagenRepository.save(newImage);
        System.out.println("Imagen importada y guardada en DB: " + savedImage.getUrlImagen()); // Depuración
        return savedImage;
    }

    public List<String> getAllImageUrls() {
        List<String> urls = galeriaImagenRepository.findByVisibleTrue()
                .stream()
                .map(GaleriaImagen::getUrlImagen)
                .toList();
        System.out.println("Método getAllImageUrls llamado. " + urls.size() + " URLs encontradas."); // Depuración
        return urls;
    }

    public List<String> getHiddenImageUrls() {
        List<String> urls = galeriaImagenRepository.findByVisibleFalse()
                .stream()
                .map(GaleriaImagen::getUrlImagen)
                .toList();
        System.out.println("Método getHiddenImageUrls llamado. " + urls.size() + " URLs encontradas."); // Depuración
        return urls;
    }

    // --- MODIFICACIÓN CLAVE AQUÍ ---
    // Este método ahora filtrará para obtener solo las imágenes del folder 'ludicamente_uploads'
    public List<String> getAllImagesFromCloudinary() throws Exception {
        List<String> imageUrls = new ArrayList<>();
        System.out.println("Iniciando la búsqueda de imágenes en Cloudinary para el folder 'ludicamente_uploads'..."); // Depuración
        try {
            ApiResponse result = cloudinary.search()
                    // La expresión de búsqueda ahora incluye el filtro por carpeta
                    .expression("resource_type:image AND folder:ludicamente_uploads") // <-- ¡CAMBIO CLAVE AQUÍ!
                    .maxResults(500) // Puedes ajustar el número de resultados máximos según tus necesidades
                    .execute();

            List<Map<String, Object>> resources = (List<Map<String, Object>>) result.get("resources");

            if (resources != null) {
                for (Map<String, Object> resource : resources) {
                    imageUrls.add((String) resource.get("secure_url"));
                }
            }
            System.out.println("Búsqueda en Cloudinary finalizada. " + imageUrls.size() + " URLs encontradas en 'ludicamente_uploads'."); // Depuración
        } catch (Exception e) {
            System.err.println("Error al listar imágenes de Cloudinary en el servicio: " + e.getMessage()); // Depuración
            throw new Exception("No se pudieron obtener las imágenes de Cloudinary: " + e.getMessage(), e);
        }
        return imageUrls;
    }

    public boolean hideImage(String imageUrl) {
        System.out.println("Intentando ocultar imagen: " + imageUrl); // Depuración
        Optional<GaleriaImagen> optionalImage = galeriaImagenRepository.findByUrlImagen(imageUrl);
        if (optionalImage.isPresent()) {
            GaleriaImagen imagen = optionalImage.get();
            imagen.setVisible(false);
            galeriaImagenRepository.save(imagen);
            System.out.println("Imagen oculta con éxito: " + imageUrl); // Depuración
            return true;
        }
        System.out.println("Imagen no encontrada para ocultar: " + imageUrl); // Depuración
        return false;
    }

    public boolean showImage(String imageUrl) {
        System.out.println("Intentando mostrar imagen: " + imageUrl); // Depuración
        Optional<GaleriaImagen> optionalImage = galeriaImagenRepository.findByUrlImagen(imageUrl);
        if (optionalImage.isPresent()) {
            GaleriaImagen imagen = optionalImage.get();
            imagen.setVisible(true);
            galeriaImagenRepository.save(imagen);
            System.out.println("Imagen mostrada con éxito: " + imageUrl); // Depuración
            return true;
        }
        System.out.println("Imagen no encontrada para mostrar: " + imageUrl); // Depuración
        return false;
    }
}