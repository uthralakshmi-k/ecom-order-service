package com.practise.ops.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.practise.ops.config.ApiKeyConfig;
import com.practise.ops.config.ApiKeyFilter;
import com.practise.ops.config.TestSecurityConfig;
import com.practise.ops.constants.OrderStatus;
import com.practise.ops.dto.OrderRequestDTO;
import com.practise.ops.dto.OrderResponseDTO;
import com.practise.ops.db.model.Order;
import com.practise.ops.service.OrderService;
import com.practise.ops.common.OrderToOrderResponseDTOConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
@Import(TestSecurityConfig.class)
public class OrderControllerSliceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ApiKeyFilter apiKeyFilter; // include filter as a mock if you want to bypass or simulate

    private final String apiKeyHeader = "API-KEY";
    private final String validKey = "gbehaHlw3PhnoUucSaSx";

    @MockBean
    private OrderToOrderResponseDTOConverter orderDTOConverter;

    private Order sampleOrder;
    private OrderResponseDTO sampleOrderResponse;

    @BeforeEach
    void setup() {
        // Sample order entity (can be minimal)
        sampleOrder = new Order();
        sampleOrder.setId(1L);
        sampleOrder.setStatus(OrderStatus.PENDING);
        sampleOrder.setTotalAmount(BigDecimal.valueOf(100));

        // Sample OrderResponseDTO corresponding to the entity
        sampleOrderResponse = new OrderResponseDTO();
        sampleOrderResponse.setOrderId(1L);
        sampleOrderResponse.setStatus("CREATED");
        sampleOrderResponse.setTotalAmount(BigDecimal.valueOf(100));

     //   Mockito.when(apiKeyConfig.getApiKey()).thenReturn("test-api-key");
    }
    @Test
    void testGetOrdersWithValidApiKey() throws Exception {
        // Simulate service response if needed
        // when(orderService.listOrders(...)).thenReturn(...);

        mockMvc.perform(get("/orders")
                        .header(apiKeyHeader, validKey)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetOrdersWithInvalidApiKey() throws Exception {
        // If you don’t mock filter, it will run and return 401
        mockMvc.perform(get("/orders")
                        .header(apiKeyHeader, "wrong-key")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetOrdersWithoutApiKey() throws Exception {
        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    void testCreateOrder() throws Exception {
        OrderRequestDTO request = new OrderRequestDTO();
        request.setCustomerId(1L);
        request.setTotalPrice(BigDecimal.valueOf(100));

        // Mock the service and converter
        Mockito.when(orderService.createOrder(any(OrderRequestDTO.class))).thenReturn(sampleOrder);
        Mockito.when(orderDTOConverter.convert(any(Order.class))).thenReturn(sampleOrderResponse);

        mockMvc.perform(post("/orders/create")
                        .header(apiKeyHeader, validKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order created successfully"))
                .andExpect(jsonPath("$.data.orderId").value(1))
                .andExpect(jsonPath("$.data.status").value("CREATED"))
                .andExpect(jsonPath("$.data.totalAmount").value(100));
    }

    @Test
    void testListOrders() throws Exception {
        Mockito.when(orderService.listOrders(Optional.empty()))
                .thenReturn(Collections.singletonList(sampleOrder));
        Mockito.when(orderDTOConverter.convert(any(Order.class))).thenReturn(sampleOrderResponse);

        mockMvc.perform(get("/orders")
                        .header(apiKeyHeader, validKey)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order list retrieved successfully"))
                .andExpect(jsonPath("$.data[0].orderId").value(1));
    }

    @Test
    void testGetOrder() throws Exception {
        Mockito.when(orderService.getOrder(1L)).thenReturn(sampleOrder);
        Mockito.when(orderDTOConverter.convert(any(Order.class))).thenReturn(sampleOrderResponse);

        mockMvc.perform(get("/orders/1")
                        .header(apiKeyHeader, validKey)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order details"))
                .andExpect(jsonPath("$.data.orderId").value(1));
    }

    @Test
    void testCancelOrder() throws Exception {
        Mockito.when(orderService.cancelOrder(1L)).thenReturn(sampleOrder);
        Mockito.when(orderDTOConverter.convert(any(Order.class))).thenReturn(sampleOrderResponse);

        mockMvc.perform(post("/orders/cancel/1")
                        .header(apiKeyHeader, validKey)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Order cancelled successfully"))
                .andExpect(jsonPath("$.data.orderId").value(1));
    }
}

