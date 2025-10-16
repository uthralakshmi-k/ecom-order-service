package com.practise.ops.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderItemDTO {
    private Long productId;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;


    public OrderItemDTO(Long productId,int quantity) {
        this.productId =productId;
        this.quantity=quantity;
       // this.unitPrice=price;
    }

    public OrderItemDTO(String productName,int quantity) {
        this.productName =productName;
        this.quantity=quantity;
        //this.unitPrice=price;
    }
}
