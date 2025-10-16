package com.practise.ops.common;

import com.practise.ops.dto.OrderItemDTO;
import com.practise.ops.dto.OrderResponseDTO;
import com.practise.ops.db.model.Order;
import com.practise.ops.db.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderToOrderResponseDTOConverter {

    public  OrderResponseDTO convert(Order order) {
        if (order == null) {
            return null;
        }

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(order.getId());
        dto.setStatus(order.getStatus().name()); // assuming status is an enum
        dto.setCustomerId(order.getCustomer().getId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setShippingAddress(order.getShippingAddress());

        // Convert OrderItems to OrderItemDTO
        List<OrderItemDTO> items = order.getItems().stream()
                .map(this::convertOrderItem)
                .collect(Collectors.toList());

        dto.setItems(items);

        return dto;
    }

    private  OrderItemDTO convertOrderItem(OrderItem item) {
        OrderItemDTO itemDTO = new OrderItemDTO();
        itemDTO.setProductId(item.getProduct().getId());
        itemDTO.setQuantity(item.getQuantity());
        itemDTO.setUnitPrice(item.getUnitPrice());
        itemDTO.setProductName(item.getProduct().getName());
        itemDTO.setAmount(item.getAmount());
        return itemDTO;
    }
}
