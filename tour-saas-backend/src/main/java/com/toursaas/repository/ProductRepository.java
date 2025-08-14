package com.toursaas.repository;

import com.toursaas.entity.Product;
import com.toursaas.entity.Product.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByType(ProductType type);
    List<Product> findByDestinationContainingIgnoreCase(String destination);
    List<Product> findByNameContainingIgnoreCase(String name);
}