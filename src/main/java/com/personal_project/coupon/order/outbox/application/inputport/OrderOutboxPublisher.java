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
                    try {
                        // 해당 eventType을 처리할 Sender 찾기
                        senders.stream()
                                .filter(sender -> sender.supports(outbox.getEventType()))
                                .findFirst()
                                .ifPresent(sender -> sender.send(outbox.getPayload()));

                        outbox.markOutboxEventPending();
                        outboxOutputPort.save(outbox);
                        log.info("[Outbox] PUBLISHED로 수정완료");
                    } catch (Exception e) {
                        log.error("[Outbox] 이벤트 발행 실패 - aggregateId={}, reason={}", outboxEvent.getAggregateId(), e.getMessage(), e);
                        outbox.markOutboxEventFailed();
                        outboxOutputPort.save(outbox);
                    }
                });
    }
}