package com.example.orderserver.outbox.application.outputport;


import com.example.orderserver.outbox.domain.OutboxEvent;
import com.example.orderserver.outbox.domain.enumeration.OutboxEventStatus;

import java.util.List;
import java.util.Optional;

public interface OutboxOutputPort {
    public void save(OutboxEvent outboxEvent);

    public Optional<OutboxEvent> findByAggregateIdAndEventType(Long aggregateId, String eventType);

    public List<OutboxEvent> findByStatusIn(List<OutboxEventStatus> list);
}
