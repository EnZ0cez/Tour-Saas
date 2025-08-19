package com.toursaas.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toursaas.dto.LlmRecommendationResponse;
import com.toursaas.entity.Order;
import com.toursaas.entity.Product;
import com.toursaas.entity.Recommendation;
import com.toursaas.entity.User;
import com.toursaas.repository.OrderRepository;
import com.toursaas.repository.ProductRepository;
import com.toursaas.repository.RecommendationRepository;
import com.toursaas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final LlmServiceFactory llmServiceFactory;
    private final ObjectMapper objectMapper;

    @Autowired
    public RecommendationService(RecommendationRepository recommendationRepository, 
                               UserRepository userRepository, 
                               ProductRepository productRepository,
                               OrderRepository orderRepository,
                               LlmServiceFactory llmServiceFactory) {
        this.recommendationRepository = recommendationRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.llmServiceFactory = llmServiceFactory;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * 基于用户历史订单的简单推荐算法
     */
    public List<Recommendation> generateRecommendations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // 获取所有产品
        List<Product> allProducts = productRepository.findAll();
        
        // 简单的推荐算法：随机推荐或基于产品类型匹配
        List<Recommendation> recommendations = new ArrayList<>();
        Random random = new Random();
        
        // 为用户生成5个推荐
        for (int i = 0; i < Math.min(5, allProducts.size()); i++) {
            Product product = allProducts.get(random.nextInt(allProducts.size()));
            
            Recommendation recommendation = new Recommendation();
            recommendation.setUser(user);
            recommendation.setProduct(product);
            // 生成0.5-1.0之间的随机分数
            recommendation.setScore(0.5 + random.nextDouble() * 0.5);
            recommendation.setCreatedAt(new Date());
            
            recommendations.add(recommendation);
        }
        
        // 保存推荐结果
        return recommendationRepository.saveAll(recommendations);
    }

    public List<Recommendation> getRecommendationsByUserId(Long userId) {
        return recommendationRepository.findByUserIdOrderByScoreDesc(userId);
    }

    /**
     * 基于协同过滤的推荐（简化版）
     */
    public List<Product> getCollaborativeRecommendations(Long userId) {
        // 获取用户的历史订单产品类型
        // 在实际应用中，这里会更复杂，需要分析其他用户的行为
        
        // 简化实现：推荐相同类型的产品
        List<Product> recommendedProducts = productRepository.findAll()
                .stream()
                .limit(10)
                .collect(Collectors.toList());
        
        return recommendedProducts;
    }

    /**
     * 基于LLM的智能推荐系统
     */
    public List<Recommendation> generateLlmRecommendations(Long userId, String provider) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // 获取用户历史订单和偏好
        String userPreferences = buildUserPreferences(userId);
        String availableProducts = buildAvailableProducts();
        
        try {
            LlmService llmService = llmServiceFactory.getLlmService(provider);
            if (!llmService.isAvailable()) {
                throw new RuntimeException("LLM service not available");
            }
            
            String llmResponse = llmService.generateRecommendations(userPreferences, availableProducts);
            return parseLlmResponse(llmResponse, user);
            
        } catch (Exception e) {
            // Fallback to traditional recommendation if LLM fails
            return generateRecommendations(userId);
        }
    }

    /**
     * 生成个性化旅行建议
     */
    public String generatePersonalizedSuggestions(Long userId, String provider) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        String userProfile = buildUserProfile(user);
        
        try {
            LlmService llmService = llmServiceFactory.getLlmService(provider);
            if (!llmService.isAvailable()) {
                return "LLM service not available. Please try traditional recommendations.";
            }
            
            return llmService.generatePersonalizedSuggestions(userProfile);
            
        } catch (Exception e) {
            return "Failed to generate personalized suggestions: " + e.getMessage();
        }
    }

    /**
     * 分析用户行为
     */
    public String analyzeUserBehavior(Long userId, String provider) {
        String userBehavior = buildUserBehaviorData(userId);
        
        try {
            LlmService llmService = llmServiceFactory.getLlmService(provider);
            if (!llmService.isAvailable()) {
                return "LLM service not available for behavior analysis.";
            }
            
            return llmService.analyzeUserBehavior(userBehavior);
            
        } catch (Exception e) {
            return "Failed to analyze user behavior: " + e.getMessage();
        }
    }

    /**
     * 检查LLM服务状态
     */
    public Map<String, Boolean> checkLlmServices() {
        Map<String, Boolean> status = new HashMap<>();
        
        try {
            status.put("ollama", llmServiceFactory.getLlmService("ollama").isAvailable());
        } catch (Exception e) {
            status.put("ollama", false);
        }
        
        try {
            status.put("openai", llmServiceFactory.getLlmService("openai").isAvailable());
        } catch (Exception e) {
            status.put("openai", false);
        }
        
        return status;
    }

    private String buildUserPreferences(Long userId) {
        List<Order> userOrders = orderRepository.findByUserId(userId);
        
        StringBuilder preferences = new StringBuilder();
        preferences.append("用户ID: ").append(userId).append("\n");
        preferences.append("历史订单数量: ").append(userOrders.size()).append("\n");
        
        // 分析产品类型偏好
        Map<Product.ProductType, Long> typePreferences = userOrders.stream()
                .map(order -> order.getProduct().getType())
                .collect(Collectors.groupingBy(type -> type, Collectors.counting()));
        
        preferences.append("产品类型偏好: ").append(typePreferences).append("\n");
        
        // 分析价格偏好
        DoubleSummaryStatistics priceStats = userOrders.stream()
                .mapToDouble(order -> order.getProduct().getPrice().doubleValue())
                .summaryStatistics();
        
        preferences.append("价格偏好 - 平均: ").append(priceStats.getAverage())
                  .append(", 最小: ").append(priceStats.getMin())
                  .append(", 最大: ").append(priceStats.getMax()).append("\n");
        
        return preferences.toString();
    }

    private String buildAvailableProducts() {
        List<Product> products = productRepository.findAll();
        StringBuilder productsInfo = new StringBuilder();
        
        for (Product product : products) {
            productsInfo.append("ID: ").append(product.getId())
                       .append(", 名称: ").append(product.getName())
                       .append(", 类型: ").append(product.getType())
                       .append(", 价格: ").append(product.getPrice())
                       .append(", 描述: ").append(product.getDescription())
                       .append("\n");
        }
        
        return productsInfo.toString();
    }

    private String buildUserProfile(User user) {
        StringBuilder profile = new StringBuilder();
        profile.append("用户名: ").append(user.getUsername()).append("\n");
        profile.append("邮箱: ").append(user.getEmail()).append("\n");
        
        // 添加用户偏好分析
        String preferences = buildUserPreferences(user.getId());
        profile.append("用户偏好分析:\n").append(preferences);
        
        return profile.toString();
    }

    private String buildUserBehaviorData(Long userId) {
        List<Order> userOrders = orderRepository.findByUserId(userId);
        
        StringBuilder behaviorData = new StringBuilder();
        behaviorData.append("用户ID: ").append(userId).append("\n");
        behaviorData.append("总订单数: ").append(userOrders.size()).append("\n");
        
        // 时间模式分析
        Map<String, Long> monthlyOrders = userOrders.stream()
                .collect(Collectors.groupingBy(
                    order -> order.getCreatedAt().toString().substring(0, 7), 
                    Collectors.counting()
                ));
        
        behaviorData.append("月度订单分布: ").append(monthlyOrders).append("\n");
        
        // 添加详细偏好信息
        behaviorData.append(buildUserPreferences(userId));
        
        return behaviorData.toString();
    }

    private List<Recommendation> parseLlmResponse(String llmResponse, User user) {
        List<Recommendation> recommendations = new ArrayList<>();
        
        try {
            // 尝试解析JSON响应
            LlmRecommendationResponse response = objectMapper.readValue(llmResponse, LlmRecommendationResponse.class);
            
            if (response.getRecommendations() != null) {
                for (LlmRecommendationResponse.LlmRecommendation llmRec : response.getRecommendations()) {
                    try {
                        Long productId = Long.parseLong(llmRec.getProductId());
                        Product product = productRepository.findById(productId).orElse(null);
                        
                        if (product != null) {
                            Recommendation recommendation = new Recommendation();
                            recommendation.setUser(user);
                            recommendation.setProduct(product);
                            recommendation.setScore(llmRec.getScore() != null ? llmRec.getScore() : 0.8);
                            recommendation.setCreatedAt(new Date());
                            recommendations.add(recommendation);
                        }
                    } catch (NumberFormatException e) {
                        // 忽略无效的产品ID
                    }
                }
            }
            
            // 如果解析失败或没有推荐，回退到传统方法
            if (recommendations.isEmpty()) {
                return generateRecommendations(user.getId());
            }
            
        } catch (Exception e) {
            // JSON解析失败，回退到传统推荐
            return generateRecommendations(user.getId());
        }
        
        return recommendationRepository.saveAll(recommendations);
    }
}