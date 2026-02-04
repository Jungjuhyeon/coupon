package com.example.orderserver.outbox.application.inputport;

import com.example.orderserver.outbox.application.outputport.OutboxOutputPort;
import com.example.orderserver.outbox.domain.OutboxEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderOutboxSaver {
    private final OutboxOutputPort outboxOutputPort;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void saveOutboxEvent(OutboxEvent event){
        log.info("[Outbox] BEFORE_COMMIT - 저장");
        outboxOutputPort.save(event); // 같은 트랜잭션 내에서 저장됨
    }

}
