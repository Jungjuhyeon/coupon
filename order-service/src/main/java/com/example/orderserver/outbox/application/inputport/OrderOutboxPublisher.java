package com.example.orderserver.outbox.application.inputport;

import com.example.orderserver.outbox.application.outputport.OutboxEventSender;
import com.example.orderserver.outbox.application.outputport.OutboxOutputPort;
import com.example.orderserver.outbox.application.service.OutboxEventStatusHandler;
import com.example.orderserver.outbox.domain.OutboxEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderOutboxPublisher {

    private final OutboxOutputPort outboxOutputPort;
    private final OutboxEventStatusHandler statusHandler;
    private final List<OutboxEventSender> senders; // 도메인별 발행 로직 목록

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishOutboxEvent(OutboxEvent outboxEvent) {
        log.info("[Outbox] AFTER_COMMIT - Kafka 발행 시작");

        outboxOutputPort.findByAggregateIdAndEventType(outboxEvent.getAggregateId(), outboxEvent.getEventType())
                .ifPresent(outbox -> {
                    senders.stream()
                            .filter(sender -> sender.supports(outbox.getEventType()))
                            .findFirst()
                            .ifPresent(sender ->
                                    sender.send(outbox.getPayload())
                                            .thenAccept(result -> statusHandler.markPublished(outbox)) // 위임
                                            .exceptionally(ex -> {
                                                statusHandler.markFailed(outbox, ex); // 위임
                                                return null;
                                            })
                            );
                });
    }
}