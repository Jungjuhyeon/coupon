package com.personal_project.coupon.order.application.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal_project.coupon.order.application.outputport.OrderOutboxOutputPort;
import com.personal_project.coupon.order.domain.model.event.OrderCreatedEvent;
import com.personal_project.coupon.order.framwork.kafkaadapter.OrderCreatedProducer;
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

    private final OrderOutboxOutputPort orderOutboxOutputPort;
    private final ObjectMapper objectMapper;
    private final OrderCreatedProducer orderCreatedProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishAfterCommit(OrderCreatedEvent event) {
        log.info("[Outbox] AFTER_COMMIT - Kafka 발행 시작");

        orderOutboxOutputPort.findByAggregateIdAndEventType(event.getOrderId(), event.getEventType())
                .ifPresent(outbox -> {
                    try {
                        // payload 복원 후 Kafka 발행
                        OrderCreatedEvent restoredEvent = objectMapper.readValue(outbox.getPayload(), OrderCreatedEvent.class);
                        orderCreatedProducer.send(restoredEvent);

                        outbox.markOutboxEventPending();
                        orderOutboxOutputPort.save(outbox);
                    } catch (Exception e) {
                        log.error("[Outbox] 이벤트 발행 실패 - orderId={}, reason={}", event.getOrderId(), e.getMessage(), e);
                        outbox.markOutboxEventFailed();
                        orderOutboxOutputPort.save(outbox);
                    }
                });
    }
}