package com.personal_project.coupon.order.framwork.jpaadpter;

import com.personal_project.coupon.order.domain.outbox.OrderOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderOutboxJpaRepository extends JpaRepository<OrderOutbox,Long> {

    public Optional<OrderOutbox> findByAggregateIdAndEventType(Long aggregateId, String eventType);
}
