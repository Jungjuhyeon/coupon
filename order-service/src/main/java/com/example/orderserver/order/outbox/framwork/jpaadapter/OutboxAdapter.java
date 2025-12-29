package com.example.orderserver.order.outbox.framwork.jpaadapter;

import com.example.orderserver.order.outbox.application.outputport.OutboxOutputPort;
import com.example.orderserver.order.outbox.domain.OutboxEvent;
import com.example.orderserver.order.outbox.domain.enumeration.OutboxEventStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OutboxAdapter implements OutboxOutputPort {

    private final OutboxJpaRepository outboxJpaRepository;

    @Override
    public void save(OutboxEvent outboxEvent){
        outboxJpaRepository.save(outboxEvent);
    }

    @Override
    public Optional<OutboxEvent> findByAggregateIdAndEventType(Long aggregateId, String eventType){
        return outboxJpaRepository.findByAggregateIdAndEventType(aggregateId,eventType);
    }

    @Override
    public List<OutboxEvent> findByStatusIn(List<OutboxEventStatus> list){
        return outboxJpaRepository.findByStatusIn(list);
    }
}
