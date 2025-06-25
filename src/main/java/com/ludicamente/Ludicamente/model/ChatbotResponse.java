
package com.ludicamente.Ludicamente.model;

import jakarta.persistence.Entity; // Importa la anotación @Entity para marcar la clase como una entidad JPA
import jakarta.persistence.GeneratedValue; // Importa la anotación para la estrategia de generación de valores de ID
import jakarta.persistence.GenerationType; // Importa el tipo de estrategia de generación (AUTO, IDENTITY, etc.)
import jakarta.persistence.Id; // Importa la anotación @Id para marcar la clave primaria
import jakarta.persistence.Column; // Importa la anotación @Column para mapear propiedades a columnas de la tabla
import jakarta.persistence.Table; // Importa la anotación @Table para especificar el nombre de la tabla en la BD
import lombok.Getter; // Importa Getter de Lombok para generar automáticamente los métodos getter
import lombok.Setter; // Importa Setter de Lombok para generar automáticamente los métodos setter
import lombok.NoArgsConstructor; // Importa NoArgsConstructor de Lombok para generar un constructor sin argumentos
import lombok.AllArgsConstructor; // Importa AllArgsConstructor de Lombok para generar un constructor con todos los argumentos

/**
 * Entidad JPA que mapea a la tabla 'chatbot_responses' en la base de datos.
 * Esta clase representa una respuesta predeterminada del chatbot, incluyendo
 * las palabras clave que la disparan y el texto de la respuesta en sí.
 *
 * Utiliza Lombok para reducir el boilerplate de getters, setters y constructores.
 */
@Entity // Indica que esta clase es una entidad JPA y se mapeará a una tabla de base de datos
@Table(name = "chatbot_responses") // Especifica el nombre de la tabla en la base de datos
@Getter // Genera automáticamente todos los métodos getter para los campos de la clase
@Setter // Genera automáticamente todos los métodos setter para los campos de la clase
@NoArgsConstructor // Genera un constructor sin argumentos, requerido por JPA
@AllArgsConstructor // Genera un constructor con todos los argumentos (útil para la inicialización)
public class ChatbotResponse {

    @Id // Marca este campo como la clave primaria de la entidad
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura la generación automática del ID
    // IDENTITY: usa columnas de auto-incremento de la base de datos (ej. AUTO_INCREMENT en MySQL)
    private Integer id; // El ID de la respuesta, mapeado a un INT en la BD

    @Column(name = "keywords", columnDefinition = "TEXT", nullable = false)
    // Mapea a la columna 'keywords'. Usamos TEXT para almacenar múltiples palabras clave separadas por comas.
    // nullable = false indica que esta columna no puede ser nula en la BD.
    private String keywords; // Cadena de palabras clave (ej. "hola,saludos,que tal")

    @Column(name = "response_text", columnDefinition = "TEXT", nullable = false)
    // Mapea a la columna 'response_text'. Usamos TEXT para almacenar respuestas largas.
    // nullable = false indica que esta columna no puede ser nula en la BD.
    private String responseText; // El texto de la respuesta predeterminada del bot

    /**
     * Sobreescribe el método toString() para proporcionar una representación
     * legible del objeto ChatbotResponse, útil para logging y depuración.
     * @return Una cadena que representa el objeto ChatbotResponse.
     */
    @Override
    public String toString() {
        return "ChatbotResponse{" +
                "id=" + id +
                ", keywords='" + keywords + '\'' +
                ", responseText='" + responseText + '\'' +
                '}';
    }
}