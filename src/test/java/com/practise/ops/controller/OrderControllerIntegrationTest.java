package com.practise.ops.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practise.ops.constants.OrderStatus;
import com.practise.ops.db.model.*;
import com.practise.ops.db.repository.*;
import com.practise.ops.common.ApiResponse;
import com.practise.ops.dto.OrderItemDTO;
import com.practise.ops.dto.OrderRequestDTO;
import com.practise.ops.dto.OrderResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Slf4j
@Disabled
class OrderControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String apiKey = "gbehaHlw3PhnoUucSaSx";

    private Customer testCustomer;
    private Product product1;
    private Product product2;
    private Order order1;
    private Order order2;

    @BeforeEach
    void setUp() {
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        customerRepository.deleteAll();
        productRepository.deleteAll();

        // Customer
        testCustomer = new Customer();
        testCustomer.setName("Alice");
        customerRepository.save(testCustomer);

        // Products
        product1 = new Product();
        product1.setName("Product A");
        product1.setPrice(BigDecimal.valueOf(50));
        product1.setStockQuantity(10);
        productRepository.save(product1);

        product2 = new Product();
        product2.setName("Product B");
        product2.setPrice(BigDecimal.valueOf(100));
        product2.setStockQuantity(10);
        productRepository.save(product2);

        // Orders
        order1 = new Order();
        order1.setCustomer(testCustomer);
        order1.setStatus(OrderStatus.PENDING);
        order1.setTotalAmount(BigDecimal.valueOf(150));
        order1.setCreatedDate(LocalDateTime.now());
        orderRepository.save(order1);

        order2 = new Order();
        order2.setCustomer(testCustomer);
        order2.setStatus(OrderStatus.PROCESSING);
        order2.setTotalAmount(BigDecimal.valueOf(200));
        order2.setCreatedDate(LocalDateTime.now());
        orderRepository.save(order2);

        // OrderItems
        OrderItem item1 = new OrderItem();
        item1.setOrder(order1);
        item1.setProduct(product1);
        item1.setQuantity(1);
        item1.setUnitPrice(product1.getPrice());
        orderItemRepository.save(item1);

        OrderItem item2 = new OrderItem();
        item2.setOrder(order1);
        item2.setProduct(product2);
        item2.setQuantity(1);
        item2.setUnitPrice(product2.getPrice());
        orderItemRepository.save(item2);

        OrderItem item3 = new OrderItem();
        item3.setOrder(order2);
        item3.setProduct(product2);
        item3.setQuantity(2);
        item3.setUnitPrice(product2.getPrice());
        BigDecimal itemTotal = item3.getUnitPrice().multiply(BigDecimal.valueOf(item3.getQuantity()));
        item3.setAmount(itemTotal);
        orderItemRepository.save(item3);
    }

    private String getOrdersUrl() {
        return "http://localhost:" + port + "/orders";
    }

    private String getCancelUrl(Long orderId) {
        return "http://localhost:" + port + "/orders/cancel/" + orderId;
    }
    private String getCreateOrderUrl() {
        return "http://localhost:" + port + "/orders/create";
    }
    // ----------------- LIST ORDERS -----------------
    @Test
    void listOrders_withValidApiKey_shouldReturnOrdersWithItems() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("API-KEY", apiKey);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getOrdersUrl(),
                HttpMethod.GET,
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ApiResponse<List<OrderResponseDTO>> apiResponse = objectMapper.readValue(
                response.getBody(),
                new TypeReference<>() {}
        );

        assertThat(apiResponse.message()).isEqualTo("Order list retrieved successfully");
        assertThat(apiResponse.data()).hasSize(2);

        OrderResponseDTO firstOrder = apiResponse.data().get(0);
        assertThat(firstOrder.getItems()).isNotNull();
        assertThat(firstOrder.getItems().size()).isGreaterThan(0);
    }

    @Test
    void listOrders_withInvalidApiKey_shouldReturnUnauthorized() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("API-KEY", "wrong-key");
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getOrdersUrl(),
                HttpMethod.GET,
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    // ----------------- CANCEL ORDER -----------------
    @Test
    void cancelOrder_withValidApiKey_shouldCancelOrder() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("API-KEY", apiKey);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getCancelUrl(order1.getId()),
                HttpMethod.POST,
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ApiResponse<?> apiResponse = objectMapper.readValue(response.getBody(), ApiResponse.class);
        assertThat(apiResponse.message()).isEqualTo("Order cancelled successfully");

        Order updated = orderRepository.findById(order1.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    void createOrder_withValidApiKey_shouldReturnCreatedOrder() throws Exception {
        OrderItemDTO item1 = new OrderItemDTO(product1.getId(), 1);
        OrderItemDTO item2 = new OrderItemDTO(product2.getId(), 2);
        OrderRequestDTO orderRequest = new OrderRequestDTO(testCustomer.getId(), List.of(item1, item2));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("API-KEY", apiKey);


        HttpEntity<OrderRequestDTO> request = new HttpEntity<>(orderRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                getCreateOrderUrl(),
                HttpMethod.POST,
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Deserialize response
        ApiResponse<OrderResponseDTO> apiResponse = objectMapper.readValue(
                response.getBody(),
                new TypeReference<>() {}
        );

        assertThat(apiResponse.message()).isEqualTo("Order created successfully");
        assertThat(apiResponse.data()).isNotNull();
        assertThat(apiResponse.data().getCustomerId()).isEqualTo(testCustomer.getId());
        assertThat(apiResponse.data().getItems()).hasSize(2);



    }


}
