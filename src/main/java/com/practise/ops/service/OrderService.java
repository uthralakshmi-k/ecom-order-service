package com.practise.ops.service;

import com.practise.ops.dto.OrderItemDTO;
import com.practise.ops.dto.OrderRequestDTO;
import com.practise.ops.constants.OrderStatus;
import com.practise.ops.exception.OrderNotFoundException;
import com.practise.ops.db.model.Customer;
import com.practise.ops.db.model.Order;
import com.practise.ops.db.model.OrderItem;
import com.practise.ops.db.model.Product;
import com.practise.ops.db.repository.CustomerRepository;
import com.practise.ops.db.repository.OrderItemRepository;
import com.practise.ops.db.repository.OrderRepository;
import com.practise.ops.db.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;


    @Transactional
    public Order createOrder(OrderRequestDTO requestDTO) {
        // Step 1: Find the customer
        List<OrderItemDTO> itemsDTO =requestDTO.getItems();
        Customer customer = customerRepository.findById(requestDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setCreatedDate(LocalDateTime.now());
        order.setUpdatedDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setShippingAddress(requestDTO.getShippingAddress());
        BigDecimal orderTotal = BigDecimal.ZERO;
        for (OrderItemDTO dto : itemsDTO) {
            Product product = productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStockQuantity() < dto.getQuantity())
                throw new RuntimeException("Insufficient stock for " + product.getName());

            product.setStockQuantity(product.getStockQuantity() - dto.getQuantity());
            productRepository.save(product);

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setProduct(product);
            item.setQuantity(dto.getQuantity());
            item.setUnitPrice(product.getPrice());
            BigDecimal itemTotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            item.setAmount(itemTotal);
            order.getItems().add(item);
            orderTotal = orderTotal.add(item.getAmount());
        }

        order.setTotalAmount(orderTotal);
        orderRepository.save(order);

        return orderRepository.save(order);
    }


    public Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    public List<Order> listOrders(Optional<OrderStatus> status) {
        if (status.isPresent()) {
            return orderRepository.findByStatus(status.get());
        }
        return orderRepository.findAll();
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = getOrder(orderId);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only PENDING orders can be cancelled");
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdatedDate(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Transactional
    public void updatePendingOrdersToProcessing() {
        List<Order> pendingOrders = orderRepository.findByStatus(OrderStatus.PENDING);
        for (Order order : pendingOrders) {
            order.setStatus(OrderStatus.PROCESSING);
            order.setUpdatedDate(LocalDateTime.now());
        }
        orderRepository.saveAll(pendingOrders);
    }


}

