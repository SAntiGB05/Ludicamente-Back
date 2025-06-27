// src/main/java/com/ludicamente/Ludicamente/service/ImageUploadService.java
package com.ludicamente.Ludicamente.service; // Ajusta el paquete a tu estructura

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils; // Necesario para ObjectUtils.asMap() si lo usas
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects; // Para Objects.requireNonNull

@Service
public class ImageUploadService {

    @Autowired
    private Cloudinary cloudinary; // Spring inyectará el bean de Cloudinary que creaste

    public String uploadImage(MultipartFile multipartFile) throws IOException {
        try {
            // Convierte MultipartFile a File temporalmente
            // Cloudinary prefiere File o byte[] para la subida directa.
            // MultipartFile.getBytes() es una buena opción si no quieres un archivo temporal.
            // Para simplicidad y evitar archivos temporales en disco, usaremos getBytes().
            // Alternativamente, podrías usar una solución más robusta para archivos temporales si manejas archivos muy grandes.

            Map<String, Object> options = new HashMap<>();
            options.put("folder", "ludicamente_uploads"); // Opcional: Define una carpeta en Cloudinary
            options.put("resource_type", "auto"); // Detecta automáticamente si es imagen/video/etc.

            // Realiza la subida usando los bytes del archivo
            Map uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), options);

            // Devuelve la URL segura de la imagen
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            // Loguear el error para depuración
            System.err.println("Error al subir imagen a Cloudinary: " + e.getMessage());
            throw new IOException("No se pudo subir la imagen a Cloudinary", e);
        }
    }
    /*
    // Si prefieres usar archivos temporales (puede ser más robusto para archivos muy grandes, pero requiere manejo de archivos)
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        }
        return convFile;
    }
    */
}