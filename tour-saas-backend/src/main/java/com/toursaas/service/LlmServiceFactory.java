package com.toursaas.service;

import com.toursaas.config.LlmConfigProperties;
import com.toursaas.service.impl.OllamaService;
import com.toursaas.service.impl.OpenAIService;
import com.toursaas.service.impl.SpringAIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LlmServiceFactory {
    
    private final LlmConfigProperties config;
    private final OllamaService ollamaService;
    private final OpenAIService openAIService;
    private final SpringAIService springAIService;
    
    @Autowired
    public LlmServiceFactory(LlmConfigProperties config, 
                           OllamaService ollamaService, 
                           OpenAIService openAIService,
                           SpringAIService springAIService) {
        this.config = config;
        this.ollamaService = ollamaService;
        this.openAIService = openAIService;
        this.springAIService = springAIService;
    }
    
    public LlmService getLlmService() {
        return switch (config.getProvider().toLowerCase()) {
            case "ollama" -> ollamaService;
            case "openai" -> openAIService;
            case "spring" -> springAIService;
            default -> throw new IllegalArgumentException("Unsupported LLM provider: " + config.getProvider());
        };
    }
    
    public LlmService getLlmService(String provider) {
        return switch (provider.toLowerCase()) {
            case "ollama" -> ollamaService;
            case "openai" -> openAIService;
            case "spring" -> springAIService;
            default -> throw new IllegalArgumentException("Unsupported LLM provider: " + provider);
        };
    }
}