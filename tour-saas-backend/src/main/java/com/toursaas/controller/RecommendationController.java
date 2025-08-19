package com.toursaas.controller;

import com.toursaas.dto.LlmRequest;
import com.toursaas.entity.Product;
import com.toursaas.entity.Recommendation;
import com.toursaas.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping("/generate/{userId}")
    public ResponseEntity<List<Recommendation>> generateRecommendations(@PathVariable Long userId) {
        List<Recommendation> recommendations = recommendationService.generateRecommendations(userId);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getRecommendationsByUserId(@PathVariable Long userId) {
        List<Recommendation> recommendations = recommendationService.getRecommendationsByUserId(userId);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/collaborative/{userId}")
    public ResponseEntity<List<Product>> getCollaborativeRecommendations(@PathVariable Long userId) {
        List<Product> recommendedProducts = recommendationService.getCollaborativeRecommendations(userId);
        return ResponseEntity.ok(recommendedProducts);
    }

    // LLM-powered recommendation endpoints
    @PostMapping("/llm/generate/{userId}")
    public ResponseEntity<List<Recommendation>> generateLlmRecommendations(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "ollama") String provider) {
        List<Recommendation> recommendations = recommendationService.generateLlmRecommendations(userId, provider);
        return ResponseEntity.ok(recommendations);
    }

    @PostMapping("/llm/suggestions/{userId}")
    public ResponseEntity<String> generatePersonalizedSuggestions(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "ollama") String provider) {
        String suggestions = recommendationService.generatePersonalizedSuggestions(userId, provider);
        return ResponseEntity.ok(suggestions);
    }

    @PostMapping("/llm/analyze/{userId}")
    public ResponseEntity<String> analyzeUserBehavior(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "ollama") String provider) {
        String analysis = recommendationService.analyzeUserBehavior(userId, provider);
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/llm/status")
    public ResponseEntity<Map<String, Boolean>> checkLlmServices() {
        Map<String, Boolean> status = recommendationService.checkLlmServices();
        return ResponseEntity.ok(status);
    }

    @PostMapping("/llm/custom")
    public ResponseEntity<String> customLlmRequest(@RequestBody LlmRequest request) {
        try {
            String response = switch (request.getProvider().toLowerCase()) {
                case "ollama", "openai" -> 
                    recommendationService.generatePersonalizedSuggestions(
                        Long.parseLong(request.getUserId()), 
                        request.getProvider()
                    );
                default -> throw new IllegalArgumentException("Unsupported provider: " + request.getProvider());
            };
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error processing request: " + e.getMessage());
        }
    }
}