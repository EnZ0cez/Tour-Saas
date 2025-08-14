package com.toursaas.repository;

import com.toursaas.entity.Recommendation;
import com.toursaas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByUserOrderByScoreDesc(User user);
    List<Recommendation> findByUserIdOrderByScoreDesc(Long userId);
}