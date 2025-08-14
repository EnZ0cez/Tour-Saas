package com.toursaas.mapper;

import com.toursaas.entity.Order;
import com.toursaas.entity.Product;
import com.toursaas.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    
    /**
     * 毫秒级多表联合查询（订单-用户-产品）
     */
    List<Map<String, Object>> getOrderDetails(@Param("userId") Long userId, @Param("status") String status);
    
    interface OrderDetail {
        // 订单信息
        Long getOrderId();
        Double getTotalPrice();
        String getOrderStatus();
        java.util.Date getTravelDate();
        Integer getTravelersCount();
        
        // 用户信息
        Long getUserId();
        String getUsername();
        String getEmail();
        String getUserFullName();
        
        // 产品信息
        Long getProductId();
        String getProductName();
        Double getProductPrice();
        String getDestination();
        Integer getDuration();
    }
}