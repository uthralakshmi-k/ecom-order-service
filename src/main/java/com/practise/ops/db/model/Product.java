package com.practise.ops.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    private int stockQuantity;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    public Product( String name, BigDecimal price,int stockQuantity){
        this.name=name;
        this.price=price;
        this.stockQuantity =stockQuantity;
    }
}

