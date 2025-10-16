package com.personal_project.coupon.order.outbox.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal_project.coupon.order.outbox.application.outputport.OutboxOutputPort;
import com.personal_project.coupon.order.domain.model.event.OrderCreatedEvent;
import com.personal_project.coupon.order.framwork.kafkaadapter.OrderCreatedProducer;
import com.personal_project.coupon.order.outbox.domain.OutboxEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderOutboxPublisher {

    private final OutboxOutputPort outboxOutputPort;
    private final ObjectMapper objectMapper;
    private final OrderCreatedProducer orderCreatedProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishAfterCommit(OutboxEvent outboxEvent) {
        log.info("[Outbox] AFTER_COMMIT - Kafka 발행 시작");

        outboxOutputPort.findByAggregateIdAndEventType(outboxEvent.getAggregateId(), outboxEvent.getEventType())
                .ifPresent(outbox -> {
                    try {
                        // payload 복원 후 Kafka 발행
                        OrderCreatedEvent restoredEvent = objectMapper.readValue(outbox.getPayload(), OrderCreatedEvent.class);
                        orderCreatedProducer.send(restoredEvent);

                        outbox.markOutboxEventPending();
                        outboxOutputPort.save(outbox);
                    } catch (Exception e) {
                        log.error("[Outbox] 이벤트 발행 실패 - orderId={}, reason={}", outboxEvent.getAggregateId(), e.getMessage(), e);
                        outbox.markOutboxEventFailed();
                        outboxOutputPort.save(outbox);
                    }
                });
    }
}