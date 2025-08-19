package com.toursaas.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "llm")
public class LlmConfigProperties {
    
    private String provider = "ollama";
    private String model = "llama3.2";
    private String baseUrl = "http://localhost:11434";
    private String apiKey = "";
    private Integer maxTokens = 1000;
    private Double temperature = 0.7;
    private String timeout = "30s";
    
    // OpenAI specific settings
    private String openaiModel = "gpt-3.5-turbo";
    
    // Ollama specific settings
    private String ollamaModel = "llama3.2";
    private String ollamaBaseUrl = "http://localhost:11434";
}