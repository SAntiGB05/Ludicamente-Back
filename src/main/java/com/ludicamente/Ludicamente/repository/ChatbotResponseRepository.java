package com.ludicamente.Ludicamente.repository;

import com.ludicamente.Ludicamente.model.ChatbotResponse;
import org.springframework.data.jpa.repository.JpaRepository; // Importa JpaRepository
import org.springframework.stereotype.Repository; // Importa la anotación @Repository

@Repository // Marca esta interfaz como un componente de repositorio de Spring
public interface ChatbotResponseRepository extends JpaRepository<ChatbotResponse, Integer> {
    // Spring Data JPA generará automáticamente las implementaciones de los métodos CRUD
    // como save(), findById(), findAll(), delete(), etc., sin necesidad de escribirlos.

    // Si en el futuro necesitas consultas más complejas (ej. buscar por una palabra clave específica),
    // podrías añadir métodos aquí, y Spring Data JPA los implementaría por ti siguiendo ciertas convenciones
    // o con anotaciones @Query.
    // Ejemplo:
    // List<ChatbotResponse> findByKeywordsContainingIgnoreCase(String keyword);
}
