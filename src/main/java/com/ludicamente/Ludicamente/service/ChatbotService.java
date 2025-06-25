package com.ludicamente.Ludicamente.service;

import com.ludicamente.Ludicamente.model.ChatbotResponse; // Import the ChatbotResponse entity
import com.ludicamente.Ludicamente.repository.ChatbotResponseRepository; // Import the repository for ChatbotResponse
import org.springframework.beans.factory.annotation.Autowired; // Import for dependency injection
import org.springframework.stereotype.Service; // Import @Service annotation

import java.util.Arrays; // Used for splitting the keywords string
import java.util.List; // Used for lists of responses and keywords
import java.util.stream.Collectors; // Used for stream operations on lists

/**
 * Service class for the chatbot logic.
 * This class handles the business logic for retrieving predetermined responses
 * based on user input and managing chatbot responses in the database.
 */
@Service // Marks this class as a Spring service component
public class ChatbotService {

    private final ChatbotResponseRepository chatbotResponseRepository; // Dependency on the repository

    /**
     * Constructor for ChatbotService. Spring will automatically inject
     * an instance of ChatbotResponseRepository.
     * @param chatbotResponseRepository The repository for ChatbotResponse entities.
     */
    @Autowired // Marks the constructor for Spring's dependency injection
    public ChatbotService(ChatbotResponseRepository chatbotResponseRepository) {
        this.chatbotResponseRepository = chatbotResponseRepository;
    }

    /**
     * Retrieves a predetermined chatbot response based on the user's message.
     * It searches through all stored responses for keywords that match the user's input.
     * The search is case-insensitive and trims whitespace.
     *
     * @param userMessage The message sent by the user.
     * @return The predetermined response text from the bot, or a default message
     * if no matching keywords are found.
     */
    public String getBotResponse(String userMessage) {
        // Clean the user's message: convert to lowercase and trim leading/trailing whitespace.
        String cleanUserMessage = userMessage.toLowerCase().trim();

        // Retrieve all predetermined responses from the database.
        List<ChatbotResponse> allResponses = chatbotResponseRepository.findAll();

        // Iterate over each stored response to find a match.
        for (ChatbotResponse response : allResponses) {
            // Split the keywords associated with the current response (assuming they are comma-separated).
            // Then, clean each individual keyword (trim spaces and convert to lowercase).
            List<String> keywords = Arrays.stream(response.getKeywords().toLowerCase().split(","))
                    .map(String::trim)
                    .collect(Collectors.toList());

            // Check if the cleaned user message contains any of the keywords for the current response.
            for (String keyword : keywords) {
                if (cleanUserMessage.contains(keyword)) {
                    // If a match is found, immediately return the predetermined response text.
                    return response.getResponseText();
                }
            }
        }

        // If no matching keywords are found after checking all responses,
        // return a default fallback message.
        return "Lo siento, no pude entender tu pregunta. Por favor, intenta reformular o consulta nuestra sección de ayuda.";
    }

    /**
     * Saves a new chatbot response or updates an existing one in the database.
     *
     * @param response The ChatbotResponse object to be saved or updated.
     * @return The saved or updated ChatbotResponse object.
     */
    public ChatbotResponse saveChatbotResponse(ChatbotResponse response) {
        return chatbotResponseRepository.save(response);
    }

    /**
     * Retrieves a list of all predetermined chatbot responses from the database.
     * This can be useful for an administrative panel to display all responses.
     *
     * @return A list of ChatbotResponse objects.
     */
    public List<ChatbotResponse> getAllChatbotResponses() {
        return chatbotResponseRepository.findAll();
    }

    /**
     * Deletes a predetermined chatbot response from the database by its ID.
     *
     * @param id The ID of the response to be deleted.
     */
    public void deleteChatbotResponse(Integer id) {
        chatbotResponseRepository.deleteById(id);
    }
}