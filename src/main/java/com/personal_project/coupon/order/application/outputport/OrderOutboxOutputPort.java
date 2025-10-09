package com.personal_project.coupon.order.application.outputport;

import com.personal_project.coupon.order.domain.outbox.OrderOutbox;

import java.util.Optional;

public interface OrderOutboxOutputPort {
    public void save(OrderOutbox orderOutbox);

    public Optional<OrderOutbox> findByAggregateIdAndEventType(Long aggregateId, String eventType);
}
