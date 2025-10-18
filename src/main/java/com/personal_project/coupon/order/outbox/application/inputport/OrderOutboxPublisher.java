package com.personal_project.coupon.order.outbox.application.inputport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal_project.coupon.order.outbox.application.outputport.OutboxEventSender;
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

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderOutboxPublisher {

    private final OutboxOutputPort outboxOutputPort;
    private final List<OutboxEventSender> senders; // 도메인별 발행 로직 목록


    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendToKafka(OutboxEvent outboxEvent) {
        log.info("[Outbox] AFTER_COMMIT - Kafka 발행 시작");

        outboxOutputPort.findByAggregateIdAndEventType(outboxEvent.getAggregateId(), outboxEvent.getEventType())
                .ifPresent(outbox -> {
                    senders.stream()
                            .filter(sender -> sender.supports(outbox.getEventType()))
                            .findFirst()
                            .ifPresent(sender ->
                                    sender.send(outbox.getPayload())
                                            .thenAccept(sendResult -> {
                                                outbox.markOutboxEventPending();
                                                outboxOutputPort.save(outbox);
                                                log.info("[Outbox] Kafka 발행 성공 - 상태 PUBLISHED");
                                            })
                                            .exceptionally(ex -> {
                                                outbox.markOutboxEventFailed();
                                                outboxOutputPort.save(outbox);
                                                log.error("[Outbox] Kafka 발행 실패 - aggregateId={}, reason={}",
                                                        outbox.getAggregateId(), ex.getMessage(), ex);
                                                return null;
                                            })
                            );
                });
    }
}