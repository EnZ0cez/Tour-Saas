package com.toursaas.dto;

import lombok.Data;
import java.util.List;

@Data
public class LlmRecommendationResponse {
    private List<LlmRecommendation> recommendations;
    private String analysis;
    private String suggestions;
    
    @Data
    public static class LlmRecommendation {
        private String productId;
        private String reason;
        private Double score;
        private List<String> highlights;
        private String season;
        private String budgetRange;
    }
}