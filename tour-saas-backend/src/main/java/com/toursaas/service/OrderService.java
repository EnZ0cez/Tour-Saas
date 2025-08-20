package com.toursaas.service;

import com.toursaas.config.RabbitMQConfig;
import com.toursaas.entity.Order;
import com.toursaas.entity.Product;
import com.toursaas.entity.User;
import com.toursaas.mapper.OrderMapper;
import com.toursaas.repository.OrderRepository;
import com.toursaas.repository.ProductRepository;
import com.toursaas.repository.UserRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final RabbitTemplate rabbitTemplate;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, UserRepository userRepository, 
                        ProductRepository productRepository, RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @CacheEvict(value = {"orders", "products"}, allEntries = true)
    @Transactional
    public Order createOrder(Long userId, Long productId, Integer travelersCount, Date travelDate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        // 计算总价
        BigDecimal totalPrice = product.getPrice().multiply(new BigDecimal(travelersCount));

        // 创建订单
        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setTotalPrice(totalPrice);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setTravelDate(travelDate);
        order.setTravelersCount(travelersCount);

        Order savedOrder = orderRepository.save(order);

        // 发送订单创建消息到RabbitMQ
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE, RabbitMQConfig.ORDER_ROUTING_KEY, savedOrder);

        return savedOrder;
    }

    @Cacheable(value = "orders", key = "#id")
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Cacheable(value = "orders", key = "'user:' + #userId")
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Cacheable(value = "orders", key = "'status:' + #status.name()")
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Cacheable(value = "orders", key = "'all'")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @CachePut(value = "orders", key = "#id")
    @CacheEvict(value = "orders", allEntries = true)
    @Transactional
    public Order updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        // 发送订单状态更新消息
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_EXCHANGE, RabbitMQConfig.ORDER_ROUTING_KEY, updatedOrder);

        return updatedOrder;
    }

    @CacheEvict(value = "orders", allEntries = true)
    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE)
    public void handleOrderMessage(Order order) {
        // 处理订单消息
        System.out.println("Received order message: " + order.getId());
        
        // 这里可以添加订单处理逻辑，如：
        // 1. 发送邮件通知
        // 2. 更新推荐系统
        // 3. 记录日志
        // 4. 触发其他业务流程
    }

    public void sendOrderNotification(Long orderId, String message) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        
        // 发送通知消息
        rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_EXCHANGE, RabbitMQConfig.NOTIFICATION_ROUTING_KEY, 
                new NotificationMessage(orderId, order.getUser().getEmail(), message));
    }

    // 通知消息内部类
    public static class NotificationMessage {
        private Long orderId;
        private String userEmail;
        private String message;
        private Date timestamp;

        public NotificationMessage(Long orderId, String userEmail, String message) {
            this.orderId = orderId;
            this.userEmail = userEmail;
            this.message = message;
            this.timestamp = new Date();
        }

        // Getters and Setters
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        public String getUserEmail() { return userEmail; }
        public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public Date getTimestamp() { return timestamp; }
        public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    }
}