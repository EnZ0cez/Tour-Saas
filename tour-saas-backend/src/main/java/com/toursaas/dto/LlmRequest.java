package com.toursaas.dto;

import lombok.Data;

@Data
public class LlmRequest {
    private String userId;
    private String userPreferences;
    private String userProfile;
    private String availableProducts;
    private String provider;
}