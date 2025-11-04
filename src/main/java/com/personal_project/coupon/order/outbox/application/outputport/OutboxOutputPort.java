package com.personal_project.coupon.order.outbox.application.outputport;

import com.personal_project.coupon.order.outbox.domain.OutboxEvent;
import com.personal_project.coupon.order.outbox.domain.enumeration.OutboxEventStatus;

import java.util.List;
import java.util.Optional;

public interface OutboxOutputPort {
    public void save(OutboxEvent outboxEvent);

    public Optional<OutboxEvent> findByAggregateIdAndEventType(Long aggregateId, String eventType);

    public List<OutboxEvent> findByStatusIn(List<OutboxEventStatus> list);
}
