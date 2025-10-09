package com.personal_project.coupon.order.application.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal_project.coupon.order.application.outputport.OrderOutboxOutputPort;
import com.personal_project.coupon.order.domain.model.event.OrderCreatedEvent;
import com.personal_project.coupon.order.domain.outbox.OrderOutbox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderOutboxEventHandler {
    private final OrderOutboxOutputPort orderOutboxOutputPort;
    private final ObjectMapper objectMapper;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleBeforeCommit(OrderCreatedEvent event) throws JsonProcessingException {
        log.info("[Outbox] BEFORE_COMMIT - 저장");

        String payload = objectMapper.writeValueAsString(event);

        OrderOutbox orderOutbox = OrderOutbox.create(event.getOrderId(),event.getEventType(),payload);

        orderOutboxOutputPort.save(orderOutbox); // 같은 트랜잭션 내에서 저장됨
    }

}
