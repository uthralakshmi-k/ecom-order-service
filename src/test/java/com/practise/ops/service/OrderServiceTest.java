package com.practise.ops.service;


import com.practise.ops.dto.OrderItemDTO;
import com.practise.ops.dto.OrderRequestDTO;
import com.practise.ops.db.model.Customer;
import com.practise.ops.db.model.Order;
import com.practise.ops.db.model.Product;
import com.practise.ops.db.repository.CustomerRepository;
import com.practise.ops.db.repository.OrderRepository;
import com.practise.ops.db.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Customer customer;
    private Product product;


    @BeforeEach
    void setup() {
        customer = new Customer();
        customer.setName("Bob");
        customer.setEmail("bob@example.com");
        customer.setMobile("8888888888");
        customerRepository.save(customer);

        product = new Product();
        product.setName("Keyboard");
        product.setPrice(BigDecimal.valueOf(1500));
        product.setStockQuantity(20);
        productRepository.save(product);
    }

    @Test
    void testPlaceOrder() {
        OrderRequestDTO orderDTO  = new OrderRequestDTO();
      //Order order = orderService.placeOrder(customer.getId(), List.of(item));
        orderDTO.setCustomerId(customer.getId());
        OrderItemDTO item = new OrderItemDTO();
        item.setProductId(product.getId());
        item.setQuantity(2);
        orderDTO.setItems(List.of(item));


        Order order = orderService.createOrder(orderDTO);
        assertThat(order.getId()).isNotNull();
        assertThat(order.getItems()).hasSize(1);
        assertThat(order.getTotalAmount()).isEqualByComparingTo("3000");
        assertThat(productRepository.findById(product.getId()).get().getStockQuantity()).isEqualTo(18);
    }

    @Test
    void testTrackOrder() {

        OrderRequestDTO orderDTO  = new OrderRequestDTO();
        //Order order = orderService.placeOrder(customer.getId(), List.of(item));
        orderDTO.setCustomerId(customer.getId());
        OrderItemDTO item = new OrderItemDTO();
        item.setProductId(product.getId());
        item.setQuantity(1);
        orderDTO.setItems(List.of(item));


        Order newOrder = orderService.createOrder(orderDTO);

        //Order fetched = orderService.trackOrder(placed.getId());
        Order fetched = orderService.getOrder(newOrder.getId());
        assertThat(fetched.getId()).isEqualTo(newOrder.getId());
    }


}
