package com.toursaas.service;

import java.util.Map;

public interface LlmService {
    
    /**
     * Generate recommendations using LLM
     * @param userPreferences user preferences and history
     * @param availableProducts available products for recommendation
     * @return LLM response with recommendations
     */
    String generateRecommendations(String userPreferences, String availableProducts);
    
    /**
     * Generate personalized travel suggestions
     * @param userProfile user profile and preferences
     * @return personalized travel suggestions
     */
    String generatePersonalizedSuggestions(String userProfile);
    
    /**
     * Analyze user behavior and preferences
     * @param userBehavior user behavior data
     * @return analysis results
     */
    String analyzeUserBehavior(String userBehavior);
    
    /**
     * Check if LLM service is available
     * @return true if service is available
     */
    boolean isAvailable();
}