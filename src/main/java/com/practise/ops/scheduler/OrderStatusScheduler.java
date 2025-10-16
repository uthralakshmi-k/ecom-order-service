//package com.practise.ops.scheduler;
//
//
//
//import com.practise.ops.service.OrderService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class OrderStatusScheduler {
//
//    private final OrderService orderService;
//
//    // Runs every 5 minutes
//    @Scheduled(fixedRate = 300000, initialDelay = 10000)
//    public void updatePendingOrders() {
//        orderService.updatePendingOrdersToProcessing();
//    }
//}
