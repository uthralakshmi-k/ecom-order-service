package com.practise.ops.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String mobile;
    private String deliveryAddress;

    public Customer (String name,String email,String mobile,String address) {
    this.name=name;
    this.email=email;
    this.mobile =mobile;
    this.deliveryAddress= address;
    }
}
