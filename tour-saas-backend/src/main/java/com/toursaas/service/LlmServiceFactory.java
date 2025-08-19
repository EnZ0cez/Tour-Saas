package com.toursaas.service;

import com.toursaas.config.LlmConfigProperties;
import com.toursaas.service.impl.OllamaService;
import com.toursaas.service.impl.OpenAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LlmServiceFactory {
    
    private final LlmConfigProperties config;
    private final OllamaService ollamaService;
    private final OpenAIService openAIService;
    
    @Autowired
    public LlmServiceFactory(LlmConfigProperties config, 
                           OllamaService ollamaService, 
                           OpenAIService openAIService) {
        this.config = config;
        this.ollamaService = ollamaService;
        this.openAIService = openAIService;
    }
    
    public LlmService getLlmService() {
        return switch (config.getProvider().toLowerCase()) {
            case "ollama" -> ollamaService;
            case "openai" -> openAIService;
            default -> throw new IllegalArgumentException("Unsupported LLM provider: " + config.getProvider());
        };
    }
    
    public LlmService getLlmService(String provider) {
        return switch (provider.toLowerCase()) {
            case "ollama" -> ollamaService;
            case "openai" -> openAIService;
            default -> throw new IllegalArgumentException("Unsupported LLM provider: " + provider);
        };
    }
}