package com.practise.ops.db.repository;

import com.practise.ops.constants.OrderStatus;
import com.practise.ops.db.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(OrderStatus orderStatus);
}
