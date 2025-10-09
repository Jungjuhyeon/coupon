package com.personal_project.coupon.order.framwork.jpaadpter;

import com.personal_project.coupon.order.application.outputport.OrderOutboxOutputPort;
import com.personal_project.coupon.order.domain.outbox.OrderOutbox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderOutboxAdapter implements OrderOutboxOutputPort {

    private final OrderOutboxJpaRepository orderOutboxJpaRepository;

    @Override
    public void save(OrderOutbox orderOutbox){
        orderOutboxJpaRepository.save(orderOutbox);
    }

    @Override
    public Optional<OrderOutbox> findByAggregateIdAndEventType(Long aggregateId, String eventType){
        return orderOutboxJpaRepository.findByAggregateIdAndEventType(aggregateId,eventType);
    }
}
