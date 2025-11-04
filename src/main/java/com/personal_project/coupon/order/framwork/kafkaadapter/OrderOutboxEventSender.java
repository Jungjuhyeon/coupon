package com.personal_project.coupon.order.framwork.kafkaadapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal_project.coupon.order.application.outputport.OrderEventOutputPort;
import com.personal_project.coupon.order.domain.model.event.OrderCreatedEvent;
import com.personal_project.coupon.order.outbox.application.outputport.OutboxEventSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderOutboxEventSender implements OutboxEventSender {

    private final ObjectMapper objectMapper;
    private final OrderEventOutputPort orderEventOutputPort;

    @Override
    public boolean supports(String eventType) {
        return "OrderCreated".equals(eventType);
    }

    @Override
    public CompletableFuture<?> send(String payload) {
        try {
            OrderCreatedEvent event = objectMapper.readValue(payload, OrderCreatedEvent.class);
            return orderEventOutputPort.send(event); // send()가 CompletableFuture 반환

        } catch (Exception e) {
            CompletableFuture<Void> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(e);
            return failedFuture;
        }
    }
}