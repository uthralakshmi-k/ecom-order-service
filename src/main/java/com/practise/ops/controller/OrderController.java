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

/**
 * Controller class that handles all operations related to Orders in the E-commerce Order Processing System.
 * <p>
 * Provides endpoints for creating, listing, retrieving, and canceling orders.
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    private final OrderToOrderResponseDTOConverter orderDTOConverter;

    /**
     * Retrieves a list of all orders.
     *
     * If a status parameter is provided, it filters orders by that status.
     *
     * @param status Optional order status to filter the orders (e.g., PENDING, PROCESSING, SHIPPED, DELIVERED).
     * @return ResponseEntity containing an ApiResponse with a list of OrderResponseDTO objects.
     *
     * Example:
     * GET /orders?status=PROCESSING
     */
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

    /**
     * Creates a new order with the provided customer ID and list of order items.
     *
     * The request body must include the customer ID and one or more products with quantities.
     *
     * @param request The order details including customer ID and list of items.
     * @return ResponseEntity containing an ApiResponse with the created OrderResponseDTO.
     *
     * Example:
     * POST /orders/create
     * {
     *   "customerId": 1,
     *   "items": [
     *     {"productId": 1, "quantity": 2},
     *     {"productId": 2, "quantity": 1}
     *   ]
     * }
     */
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(@RequestBody OrderRequestDTO request) {
        log.info(" order create");
        Order newOrder = orderService.createOrder(request);
        OrderResponseDTO response = orderDTOConverter.convert(newOrder);
        return ResponseEntity.ok(new ApiResponse<>("Order created successfully", response));
    }

    /**
     * Retrieves the details of a specific order by its ID.
     *
     * @param orderId The unique identifier of the order to retrieve.
     * @return ResponseEntity containing an ApiResponse with the OrderResponseDTO for the requested order.
     *
     * Example:
     * GET /orders/5
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrder(@PathVariable Long orderId) {

        Order newOrder = orderService.getOrder(orderId);
        OrderResponseDTO response = orderDTOConverter.convert(newOrder);

        return ResponseEntity.ok(new ApiResponse<>("Order details", response));
    }


    /**
     * Cancels an existing order, if it is still in PENDING status.
     *
     * Orders that have moved to PROCESSING or later statuses cannot be canceled.
     *
     * @param orderId The unique identifier of the order to cancel.
     * @return ResponseEntity containing an ApiResponse with the canceled OrderResponseDTO.
     *
     * Example:
     * POST /orders/cancel/5
     */
    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDTO>> cancelOrder(@PathVariable Long orderId) {
        Order canceledOrder = orderService.cancelOrder(orderId);
        OrderResponseDTO response = orderDTOConverter.convert(canceledOrder);
        log.info("cancelled order with id :{}", canceledOrder.getId());
        return ResponseEntity.ok(new ApiResponse<>("Order cancelled successfully", response));
    }

}


