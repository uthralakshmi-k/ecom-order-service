package com.practise.ops.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@NoArgsConstructor
public class OrderRequestDTO {

    private Long customerId;

    private List<OrderItemDTO> items;

    private BigDecimal totalPrice;

    private String shippingAddress;

    public OrderRequestDTO(Long id, List<OrderItemDTO> item1) {
        this.customerId=id;
        this.items=item1;
    }
}

