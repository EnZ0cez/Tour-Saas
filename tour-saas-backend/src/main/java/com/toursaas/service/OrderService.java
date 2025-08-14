package com.toursaas.service;

import com.toursaas.entity.Order;
import com.toursaas.entity.Product;
import com.toursaas.entity.User;
import com.toursaas.repository.OrderRepository;
import com.toursaas.repository.ProductRepository;
import com.toursaas.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public Order createOrder(Long userId, Long productId, Integer travelersCount, Date travelDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setTravelersCount(travelersCount);
        order.setTravelDate(travelDate);
        
        // 计算总价
        BigDecimal totalPrice = product.getPrice().multiply(new BigDecimal(travelersCount));
        order.setTotalPrice(totalPrice);
        
        order.setStatus(Order.OrderStatus.PENDING);
        order.setCreatedAt(new Date());
        order.setUpdatedAt(new Date());
        
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        
        order.setStatus(status);
        order.setUpdatedAt(new Date());
        
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}