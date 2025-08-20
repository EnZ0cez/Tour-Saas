package com.toursaas.service.impl;

import com.toursaas.config.LlmConfigProperties;
import com.toursaas.service.LlmService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpringAIService implements LlmService {
    
    private final LlmConfigProperties config;
    private final ChatClient chatClient;
    
    @Autowired
    public SpringAIService(LlmConfigProperties config, ChatModel chatModel) {
        this.config = config;
        this.chatClient = ChatClient.create(chatModel);
    }
    
    @Override
    public String generateRecommendations(String userPreferences, String availableProducts) {
        String prompt = buildRecommendationPrompt(userPreferences, availableProducts);
        return callAI(prompt);
    }
    
    @Override
    public String generatePersonalizedSuggestions(String userProfile) {
        String prompt = buildPersonalizedPrompt(userProfile);
        return callAI(prompt);
    }
    
    @Override
    public String analyzeUserBehavior(String userBehavior) {
        String prompt = buildBehaviorAnalysisPrompt(userBehavior);
        return callAI(prompt);
    }
    
    @Override
    public boolean isAvailable() {
        try {
            return chatClient.prompt()
                    .user("test")
                    .call()
                    .content() != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    private String callAI(String prompt) {
        try {
            return chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();
        } catch (Exception e) {
            throw new RuntimeException("Failed to call AI API: " + e.getMessage(), e);
        }
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