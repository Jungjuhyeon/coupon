package com.example.orderserver.order.infra.kafkaadapter;

import com.example.orderserver.order.application.outputport.OrderEventOutputPort;
import com.example.orderserver.order.domain.model.event.OrderCreatedEvent;
import com.example.orderserver.order.outbox.application.outputport.OutboxEventSender;
import com.fasterxml.jackson.databind.ObjectMapper;
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