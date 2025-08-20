package com.toursaas.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "orders")
@TableName("orders")
public class Order {
    @TableId(type = IdType.AUTO)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @TableField("user_id")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @TableField("product_id")
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @TableField("total_price")
    @Column(nullable = false)
    private BigDecimal totalPrice;

    @TableField("status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @TableField("travel_date")
    private Date travelDate;

    @TableField("travelers_count")
    private Integer travelersCount;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @TableLogic
    @TableField("deleted")
    private Integer deleted = 0;

    public enum OrderStatus {
        PENDING, PAID, CONFIRMED, COMPLETED, CANCELLED
    }
}