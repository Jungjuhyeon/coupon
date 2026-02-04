package com.example.orderserver.outbox.framwork.jpaadapter;

import com.example.orderserver.outbox.domain.OutboxEvent;
import com.example.orderserver.outbox.domain.enumeration.OutboxEventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OutboxJpaRepository extends JpaRepository<OutboxEvent,Long> {

    public Optional<OutboxEvent> findByAggregateIdAndEventType(Long aggregateId, String eventType);

    List<OutboxEvent> findByStatusIn(List<OutboxEventStatus> statuses);

}
