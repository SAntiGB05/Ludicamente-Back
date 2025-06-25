package com.ludicamente.Ludicamente.controller;

import com.ludicamente.Ludicamente.model.ChatbotResponse;
import com.ludicamente.Ludicamente.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Autowired
    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/message")
    public String getBotMessage(@RequestBody String userMessage) {
        return chatbotService.getBotResponse(userMessage);
    }

    /**
     * Endpoint para obtener todas las respuestas predeterminadas del chatbot.
     * Útil para un panel de administración.
     *
     * @return Una lista de objetos ChatbotResponse.
     */
    @GetMapping("/responses")
    public List<ChatbotResponse> getAllResponses() {
        return chatbotService.getAllChatbotResponses();
    }

    /**
     * Endpoint para crear una nueva respuesta predeterminada del chatbot.
     *
     * @param chatbotResponse El objeto ChatbotResponse a guardar.
     * @return La respuesta HTTP 200 OK con el objeto ChatbotResponse guardado.
     */
    @PostMapping("/responses")
    public ResponseEntity<ChatbotResponse> createResponse(@RequestBody ChatbotResponse chatbotResponse) {
        ChatbotResponse savedResponse = chatbotService.saveChatbotResponse(chatbotResponse);
        return ResponseEntity.ok(savedResponse);
    }

    /**
     * Endpoint para actualizar una respuesta predeterminada existente por su ID.
     *
     * @param id El ID de la respuesta a actualizar (tomado de la URL).
     * @param chatbotResponse El objeto ChatbotResponse con los datos actualizados.
     * @return La respuesta HTTP 200 OK con el objeto ChatbotResponse actualizado.
     */
    @PutMapping("/responses/{id}")
    public ResponseEntity<ChatbotResponse> updateResponse(@PathVariable Integer id, @RequestBody ChatbotResponse chatbotResponse) {
        // Aseguramos que el ID del objeto del cuerpo de la petición coincida con el ID de la URL
        chatbotResponse.setId(id);
        ChatbotResponse updatedResponse = chatbotService.saveChatbotResponse(chatbotResponse);
        return ResponseEntity.ok(updatedResponse);
    }

    /**
     * Endpoint para eliminar una respuesta predeterminada por su ID.
     *
     * @param id El ID de la respuesta a eliminar.
     * @return La respuesta HTTP 204 No Content (indica que la operación fue exitosa sin contenido de retorno).
     */
    @DeleteMapping("/responses/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable Integer id) {
        chatbotService.deleteChatbotResponse(id);
        return ResponseEntity.noContent().build();
    }
}
