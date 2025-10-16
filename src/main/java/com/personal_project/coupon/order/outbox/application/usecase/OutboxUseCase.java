package com.personal_project.coupon.order.outbox.application.usecase;


public interface OutboxUseCase {
    public void markOutboxEventProcessed(Long orderId ,String eventType);
}
