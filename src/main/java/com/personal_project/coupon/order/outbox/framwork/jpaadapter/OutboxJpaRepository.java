package com.personal_project.coupon.order.outbox.framwork.jpaadapter;

import com.personal_project.coupon.order.outbox.domain.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OutboxJpaRepository extends JpaRepository<OutboxEvent,Long> {

    public Optional<OutboxEvent> findByAggregateIdAndEventType(Long aggregateId, String eventType);
}
