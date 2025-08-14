package com.toursaas.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderDTO {
    private Long userId;
    private Long productId;
    private Integer travelersCount;
    private Date travelDate;
    private BigDecimal totalPrice;
    private String status;
}