package com.example.orderserver.outbox.application.service;


import com.example.orderserver.outbox.application.outputport.OutboxOutputPort;
import com.example.orderserver.outbox.domain.OutboxEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxEventStatusHandler {
    private final OutboxOutputPort outboxOutputPort;

    public void markPublished(OutboxEvent event) {
        event.markOutboxEventPending();
        outboxOutputPort.save(event);
        log.info("[Outbox] Kafka 발행 성공 - aggregateId={} 상태 PUBLISHED", event.getAggregateId());
    }
    public void markFailed(OutboxEvent event, Throwable ex) {
        event.markOutboxEventFailed();
        outboxOutputPort.save(event);
        log.error("[Outbox] Kafka 발행 실패 - aggregateId={}, reason={}", event.getAggregateId(), ex.getMessage(), ex);
    }
}
