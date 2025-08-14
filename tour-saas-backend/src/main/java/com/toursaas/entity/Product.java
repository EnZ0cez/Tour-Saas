package com.toursaas.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private String destination;

    private String imageUrl;

    private Integer duration; // 游玩天数

    @Enumerated(EnumType.STRING)
    private ProductType type;

    private Boolean isActive = true;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    public enum ProductType {
        DOMESTIC, INTERNATIONAL, CRUISE, HOTEL, TICKET
    }
}