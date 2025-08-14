package com.toursaas.service;

import com.toursaas.entity.Product;
import com.toursaas.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(Product product) {
        product.setCreatedAt(new java.util.Date());
        product.setUpdatedAt(new java.util.Date());
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setDestination(productDetails.getDestination());
        product.setImageUrl(productDetails.getImageUrl());
        product.setDuration(productDetails.getDuration());
        product.setType(productDetails.getType());
        product.setIsActive(productDetails.isActive());
        product.setUpdatedAt(new java.util.Date());
        
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    public List<Product> getProductsByType(Product.ProductType type) {
        return productRepository.findByType(type);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }
}