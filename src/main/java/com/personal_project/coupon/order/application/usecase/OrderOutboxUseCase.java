package com.personal_project.coupon.order.application.usecase;

import com.personal_project.coupon.order.domain.model.event.OrderCreatedEvent;

public interface OrderOutboxUseCase {
    public void markOutboxEventProcessed(OrderCreatedEvent event);
}
