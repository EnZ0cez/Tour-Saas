package com.toursaas.mapper;

import com.toursaas.entity.Order;
import com.toursaas.entity.Product;
import com.toursaas.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface OrderMapper {
    
    /**
     * 毫秒级多表联合查询（订单-用户-产品）
     */
    @Select({
        "<script>",
        "SELECT o.id as orderId, o.total_price as totalPrice, o.status as orderStatus,",
        "       o.travel_date as travelDate, o.travelers_count as travelersCount,",
        "       u.id as userId, u.username, u.email, u.full_name as userFullName,",
        "       p.id as productId, p.name as productName, p.price as productPrice,",
        "       p.destination, p.duration",
        "FROM orders o",
        "JOIN users u ON o.user_id = u.id",
        "JOIN products p ON o.product_id = p.id",
        "WHERE 1=1",
        "<if test='userId != null'> AND o.user_id = #{userId} </if>",
        "<if test='status != null'> AND o.status = #{status} </if>",
        "ORDER BY o.created_at DESC",
        "</script>"
    })
    List<OrderDetail> getOrderDetails(@Param("userId") Long userId, @Param("status") String status);
    
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