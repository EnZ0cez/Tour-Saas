package com.toursaas.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toursaas.config.LlmConfigProperties;
import com.toursaas.service.LlmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService implements LlmService {
    
    private final LlmConfigProperties config;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public OpenAIService(LlmConfigProperties config) {
        this.config = config;
        this.objectMapper = new ObjectMapper();
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + config.getApiKey())
                .build();
    }
    
    @Override
    public String generateRecommendations(String userPreferences, String availableProducts) {
        String prompt = buildRecommendationPrompt(userPreferences, availableProducts);
        return callOpenAI(prompt);
    }
    
    @Override
    public String generatePersonalizedSuggestions(String userProfile) {
        String prompt = buildPersonalizedPrompt(userProfile);
        return callOpenAI(prompt);
    }
    
    @Override
    public String analyzeUserBehavior(String userBehavior) {
        String prompt = buildBehaviorAnalysisPrompt(userBehavior);
        return callOpenAI(prompt);
    }
    
    @Override
    public boolean isAvailable() {
        try {
            return webClient.get()
                    .uri("/models")
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(response -> true)
                    .onErrorReturn(false)
                    .timeout(Duration.ofSeconds(5))
                    .block();
        } catch (Exception e) {
            return false;
        }
    }
    
    private String callOpenAI(String prompt) {
        Map<String, Object> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        
        Map<String, Object> request = new HashMap<>();
        request.put("model", config.getOpenaiModel());
        request.put("messages", List.of(message));
        request.put("max_tokens", config.getMaxTokens());
        request.put("temperature", config.getTemperature());
        
        try {
            String response = webClient.post()
                    .uri("/chat/completions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();
            
            return extractContentFromResponse(response);
        } catch (Exception e) {
            throw new RuntimeException("Failed to call OpenAI API: " + e.getMessage(), e);
        }
    }
    
    private String extractContentFromResponse(String response) {
        try {
            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                return (String) message.get("content");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse OpenAI response: " + e.getMessage(), e);
        }
        return "";
    }
    
    private String buildRecommendationPrompt(String userPreferences, String availableProducts) {
        return String.format("""
            You are a professional travel recommendation system expert. Please provide personalized travel recommendations based on user preferences and available travel products.
            
            User preferences and travel history:
            %s
            
            Available travel products:
            %s
            
            Please provide:
            1. Personalized recommendations based on user preferences
            2. Recommendation reasons and match analysis
            3. Expected experience and highlights for each recommendation
            4. Suggestions considering season, budget and other factors
            
            Please return in JSON format containing recommendations array, each recommendation includes:
            - productId: Product ID
            - reason: Recommendation reason
            - score: Match score (0-1)
            - highlights: Experience highlights
            """, userPreferences, availableProducts);
    }
    
    private String buildPersonalizedPrompt(String userProfile) {
        return String.format("""
            You are a professional travel consultant. Please provide personalized travel suggestions based on user profile.
            
            User profile:
            %s
            
            Please provide:
            1. Travel type recommendations suitable for the user
            2. Best travel time and destinations
            3. Itinerary suggestions
            4. Budget planning advice
            5. Special considerations
            
            Please provide suggestions in a friendly tone, making users feel professional personalized service.
            """, userProfile);
    }
    
    private String buildBehaviorAnalysisPrompt(String userBehavior) {
        return String.format("""
            You are a user behavior analysis expert. Please analyze user's travel behavior data to understand their preferences and needs.
            
            User behavior data:
            %s
            
            Please analyze:
            1. User's travel preference types
            2. Budget range and spending habits
            3. Travel time and season preferences
            4. Hobbies and activity types
            5. Potential needs and improvement suggestions
            
            Please return analysis results in JSON format.
            """, userBehavior);
    }
}