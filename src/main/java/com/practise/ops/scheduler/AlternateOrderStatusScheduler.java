package com.practise.ops.scheduler;

import com.practise.ops.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class AlternateOrderStatusScheduler {

    private final OrderService orderService;
    private final TaskScheduler scheduler;

    @EventListener(ApplicationReadyEvent.class)
    public void scheduleAfterStartup() {
        // Schedule after the application is fully ready
        scheduler.scheduleAtFixedRate(orderService::updatePendingOrdersToProcessing, Duration.ofMinutes(5));
    }
}
