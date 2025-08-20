package com.toursaas.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "products")
@TableName("products")
public class Product {
    @TableId(type = IdType.AUTO)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @TableField("name")
    @Column(nullable = false)
    private String name;

    @TableField("description")
    @Column(length = 1000)
    private String description;

    @TableField("price")
    @Column(nullable = false)
    private BigDecimal price;

    @TableField("destination")
    private String destination;

    @TableField("image_url")
    private String imageUrl;

    @TableField("duration")
    private Integer duration; // 游玩天数

    @TableField("type")
    @Enumerated(EnumType.STRING)
    private ProductType type;

    @TableField("is_active")
    private Boolean isActive = true;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @TableLogic
    @TableField("deleted")
    private Integer deleted = 0;

    public enum ProductType {
        DOMESTIC, INTERNATIONAL, CRUISE, HOTEL, TICKET
    }
}