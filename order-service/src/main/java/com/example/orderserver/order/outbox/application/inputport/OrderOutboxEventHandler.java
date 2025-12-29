package com.example.orderserver.order.outbox.application.inputport;

import com.example.orderserver.order.outbox.application.outputport.OutboxOutputPort;
import com.example.orderserver.order.outbox.domain.OutboxEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderOutboxEventHandler {
    private final OutboxOutputPort outboxOutputPort;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleOutboxEvent(OutboxEvent event) throws JsonProcessingException {
        log.info("[Outbox] BEFORE_COMMIT - 저장");
        outboxOutputPort.save(event); // 같은 트랜잭션 내에서 저장됨
    }

}
