package com.practise.ops.controller;


import com.practise.ops.dto.OrderRequestDTO;
import com.practise.ops.dto.OrderResponseDTO;
import com.practise.ops.common.ApiResponse;
import com.practise.ops.constants.OrderStatus;
import com.practise.ops.db.model.Order;
import com.practise.ops.service.OrderService;
import com.practise.ops.common.OrderToOrderResponseDTOConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {


    private final OrderService orderService;
    private final  OrderToOrderResponseDTOConverter orderDTOConverter;


    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> listOrders(@RequestParam(required = false) OrderStatus status) {
        log.info("list of orders");

        List<Order> orderList = orderService.listOrders(Optional.ofNullable(status));
        // Convert each Order → OrderResponseDTO using the converter
        List<OrderResponseDTO> responseList = orderList.stream()
                .map(orderDTOConverter::convert)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>("Order list retrieved successfully", responseList));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<OrderResponseDTO>>  createOrder(@RequestBody OrderRequestDTO request) {
        log.info(" order create");
        Order newOrder = orderService.createOrder(request);
        OrderResponseDTO response = orderDTOConverter.convert(newOrder);
        return ResponseEntity.ok( new ApiResponse<>("Order created successfully", response));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>>  getOrder(@PathVariable Long orderId) {

        Order newOrder = orderService.getOrder(orderId);
        OrderResponseDTO response = orderDTOConverter.convert(newOrder);

        return ResponseEntity.ok(new ApiResponse<>("Order details", response));
    }



    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>>  cancelOrder(@PathVariable Long orderId) {
        Order canceledOrder = orderService.cancelOrder(orderId);
        OrderResponseDTO response = orderDTOConverter.convert(canceledOrder);
    log.info("cancelled order with id :{}",canceledOrder.getId());
        return ResponseEntity.ok(new ApiResponse<>("Order cancelled successfully", response));
    }

}


