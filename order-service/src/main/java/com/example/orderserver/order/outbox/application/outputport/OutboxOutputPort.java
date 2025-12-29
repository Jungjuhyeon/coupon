package com.example.orderserver.order.outbox.application.outputport;


import com.example.orderserver.order.outbox.domain.OutboxEvent;
import com.example.orderserver.order.outbox.domain.enumeration.OutboxEventStatus;

import java.util.List;
import java.util.Optional;

public interface OutboxOutputPort {
    public void save(OutboxEvent outboxEvent);

    public Optional<OutboxEvent> findByAggregateIdAndEventType(Long aggregateId, String eventType);

    public List<OutboxEvent> findByStatusIn(List<OutboxEventStatus> list);
}
