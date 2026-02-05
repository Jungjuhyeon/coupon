package com.example.orderserver.order.application.service;

import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.domain.model.event.OrderCreatedEvent;
import com.example.orderserver.outbox.domain.OutboxEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventPublisher {
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;
    private static final String aggregateType = "Order";
    private static final String eventType = "OrderCreated";

    public void publishOrderCreated(Order order, Long memberId) throws JsonProcessingException {
        OrderCreatedEvent event = Order.createOrderEvent(memberId, order.getId(), order.getCouponIssueId(), eventType);
        String payload = objectMapper.writeValueAsString(event);
        OutboxEvent outboxEvent = OutboxEvent.create(aggregateType, order.getId(), event.getEventType(), payload);
        eventPublisher.publishEvent(outboxEvent);
    }
}
