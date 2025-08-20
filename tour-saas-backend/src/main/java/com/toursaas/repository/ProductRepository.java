package com.toursaas.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toursaas.entity.Product;
import com.toursaas.entity.Product.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 产品Repository - 同时支持JPA和MyBatis-Plus
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // JPA方法
    List<Product> findByType(ProductType type);
    List<Product> findByDestinationContainingIgnoreCase(String destination);
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // MyBatis-Plus功能通过单独的Mapper提供
}