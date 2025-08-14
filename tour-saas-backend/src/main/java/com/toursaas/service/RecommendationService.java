package com.toursaas.service;

import com.toursaas.entity.Product;
import com.toursaas.entity.Recommendation;
import com.toursaas.entity.User;
import com.toursaas.repository.ProductRepository;
import com.toursaas.repository.RecommendationRepository;
import com.toursaas.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public RecommendationService(RecommendationRepository recommendationRepository, 
                               UserRepository userRepository, 
                               ProductRepository productRepository) {
        this.recommendationRepository = recommendationRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
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
}