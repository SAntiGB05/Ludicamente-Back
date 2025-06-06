package com.ludicamente.Ludicamente.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class GcsService {

    private final Storage storage;

    @Value("${gcp.bucket.name}")
    private String bucketName;

    public GcsService(Storage storage) {
        this.storage = storage;
    }

    /**
     * Sube un archivo MultipartFile a Google Cloud Storage.
     *
     * @param file El archivo MultipartFile a subir desde el frontend.
     * @return La URL pública del archivo subido en GCS.
     * @throws IOException Si ocurre un error durante la lectura del archivo o la subida.
     */
    public String uploadFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("El archivo enviado está vacío.");
        }

        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String uniqueFileName = "uploads/" + UUID.randomUUID().toString() + fileExtension;

        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, uniqueFileName)
                .setContentType(file.getContentType())
                .build();

        // Subir el archivo. Los bytes del archivo se obtienen con file.getBytes().
        Blob blob = storage.create(blobInfo, file.getBytes());

        // **************** CAMBIO AQUÍ ****************
        // Ya no necesitas makePublic() porque tu bucket ya tiene 'allUsers' con 'Visualizador de objetos de Storage'.
        // Cualquier objeto subido a un bucket con esta configuración será automáticamente público.
        // Si tu cuenta de servicio 'subir-imagenes' YA TIENE el rol 'Administrador de objetos de Storage'
        // no hace falta borrar esta línea, pero es redundante y la quito por simplicidad.
        // ********************************************

        // Retorna la URL pública del archivo.
        // El formato `getMediaLink()` suele ser una URL amigable para acceso público.
        return blob.getMediaLink();
        // Otra opción de URL pública, que también es muy común y funciona con un bucket público:
        // return String.format("https://storage.googleapis.com/%s/%s", bucketName, uniqueFileName);
    }
}