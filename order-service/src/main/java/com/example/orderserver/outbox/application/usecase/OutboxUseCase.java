package com.example.orderserver.outbox.application.usecase;


public interface OutboxUseCase {
    public void markOutboxEventProcessed(Long orderId ,String eventType);
}
