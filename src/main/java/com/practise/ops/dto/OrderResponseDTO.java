package com.practise.ops.dto;

import com.practise.ops.constants.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderResponseDTO {

    private Long orderId;

    private String status;

    private Long customerId;

    private Date createdDate;

    private List<OrderItemDTO> items;

    private BigDecimal totalAmount;

    private String shippingAddress;


    public OrderResponseDTO(Long id, BigDecimal totalAmount, OrderStatus status) {
    }

    public OrderResponseDTO() {

    }
}

