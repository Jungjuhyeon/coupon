package com.example.orderserver.order.outbox.application.usecase;


public interface OutboxUseCase {
    public void markOutboxEventProcessed(Long orderId ,String eventType);
}
