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
import java.util.Map;

@Service
public class OllamaService implements LlmService {
    
    private final LlmConfigProperties config;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    
    @Autowired
    public OllamaService(LlmConfigProperties config) {
        this.config = config;
        this.objectMapper = new ObjectMapper();
        this.webClient = WebClient.builder()
                .baseUrl(config.getOllamaBaseUrl())
                .build();
    }
    
    @Override
    public String generateRecommendations(String userPreferences, String availableProducts) {
        String prompt = buildRecommendationPrompt(userPreferences, availableProducts);
        return callOllama(prompt);
    }
    
    @Override
    public String generatePersonalizedSuggestions(String userProfile) {
        String prompt = buildPersonalizedPrompt(userProfile);
        return callOllama(prompt);
    }
    
    @Override
    public String analyzeUserBehavior(String userBehavior) {
        String prompt = buildBehaviorAnalysisPrompt(userBehavior);
        return callOllama(prompt);
    }
    
    @Override
    public boolean isAvailable() {
        try {
            return webClient.get()
                    .uri("/api/tags")
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
    
    private String callOllama(String prompt) {
        Map<String, Object> request = new HashMap<>();
        request.put("model", config.getOllamaModel());
        request.put("prompt", prompt);
        request.put("stream", false);
        request.put("options", Map.of(
            "temperature", config.getTemperature(),
            "num_predict", config.getMaxTokens()
        ));
        
        try {
            return webClient.post()
                    .uri("/api/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(30))
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Failed to call Ollama API: " + e.getMessage(), e);
        }
    }
    
    private String buildRecommendationPrompt(String userPreferences, String availableProducts) {
        return String.format("""
            你是一个专业的旅游推荐系统专家。请基于用户偏好和可用的旅游产品，为用户提供个性化的旅游推荐。
            
            用户偏好和旅行历史：
            %s
            
            可用的旅游产品：
            %s
            
            请提供：
            1. 基于用户偏好的个性化推荐
            2. 推荐理由和匹配度分析
            3. 每个推荐的预计体验和亮点
            4. 考虑季节、预算等因素的建议
            
            请以JSON格式返回，包含recommendations数组，每个推荐包含：
            - productId: 产品ID
            - reason: 推荐理由
            - score: 匹配分数(0-1)
            - highlights: 体验亮点
            """, userPreferences, availableProducts);
    }
    
    private String buildPersonalizedPrompt(String userProfile) {
        return String.format("""
            你是一个专业的旅游顾问。请基于用户资料，为用户提供个性化的旅游建议。
            
            用户资料：
            %s
            
            请提供：
            1. 适合用户的旅游类型推荐
            2. 最佳旅行时间和目的地
            3. 行程安排建议
            4. 预算规划建议
            5. 特别注意事项
            
            请以友好的语气提供建议，让用户感受到专业的个性化服务。
            """, userProfile);
    }
    
    private String buildBehaviorAnalysisPrompt(String userBehavior) {
        return String.format("""
            你是一个用户行为分析专家。请分析用户的旅游行为数据，洞察用户偏好和需求。
            
            用户行为数据：
            %s
            
            请分析：
            1. 用户的旅游偏好类型
            2. 预算范围和消费习惯
            3. 旅行时间和季节偏好
            4. 兴趣爱好和活动类型
            5. 潜在需求和改进建议
            
            请以JSON格式返回分析结果。
            """, userBehavior);
    }
}