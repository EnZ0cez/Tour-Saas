package com.toursaas.controller;

import com.toursaas.entity.Product;
import com.toursaas.entity.Recommendation;
import com.toursaas.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}