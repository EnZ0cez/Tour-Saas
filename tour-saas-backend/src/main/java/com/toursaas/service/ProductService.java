package com.toursaas.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.toursaas.entity.Product;
import com.toursaas.mapper.ProductMapper;
import com.toursaas.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @CacheEvict(value = "products", allEntries = true)
    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Cacheable(value = "products", key = "'all'")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Cacheable(value = "products", key = "#id")
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @CachePut(value = "products", key = "#id")
    @CacheEvict(value = "products", key = "'all'")
    @Transactional
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
        product.setIsActive(productDetails.getIsActive());
        
        return productRepository.save(product);
    }

    @CacheEvict(value = "products", allEntries = true)
    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Cacheable(value = "products", key = "'type:' + #type.name()")
    public List<Product> getProductsByType(Product.ProductType type) {
        return productRepository.findByType(type);
    }

    @Cacheable(value = "products", key = "'search:' + #keyword")
    public List<Product> searchProducts(String keyword) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", keyword)
                   .or()
                   .like("destination", keyword)
                   .eq("is_active", true);
        return productMapper.selectList(queryWrapper);
    }

    public Page<Product> getProductsWithPagination(int page, int size) {
        Page<Product> pageParam = new Page<>(page, size);
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_active", true);
        return productMapper.selectPage(pageParam, queryWrapper);
    }

    @Cacheable(value = "products", key = "'destination:' + #destination")
    public List<Product> getProductsByDestination(String destination) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("destination", destination)
                   .eq("is_active", true);
        return productMapper.selectList(queryWrapper);
    }
}