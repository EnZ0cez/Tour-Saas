package com.toursaas.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.toursaas.entity.Order;
import com.toursaas.entity.Order.OrderStatus;
import com.toursaas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 订单Repository - 同时支持JPA和MyBatis-Plus
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // JPA方法
    List<Order> findByUser(User user);
    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(OrderStatus status);
    
    // MyBatis-Plus功能通过单独的Mapper提供
}