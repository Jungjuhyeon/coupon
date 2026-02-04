package com.example.orderserver.outbox.application.scheduler;

import com.example.orderserver.outbox.application.inputport.OrderOutboxPublisher;
import com.example.orderserver.outbox.application.outputport.OutboxOutputPort;
import com.example.orderserver.outbox.domain.OutboxEvent;
import com.example.orderserver.outbox.domain.enumeration.OutboxEventStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRetryScheduler {
    private final OutboxOutputPort outboxOutputPort;
    private final OrderOutboxPublisher outboxPublisher;

    // 10초마다 실행
    @Scheduled(fixedDelay = 10000)
    public void retryFailedOutboxEvents() {
        List<OutboxEvent> retryTargets = outboxOutputPort.findByStatusIn(
                List.of(OutboxEventStatus.READY_TO_PUBLISH, OutboxEventStatus.FAILED)
        );

        if (retryTargets.isEmpty()) return;

        log.info("[OutboxScheduler] 재시도 대상 이벤트 개수: {}", retryTargets.size());

        retryTargets.forEach(event -> {
            try {
                outboxPublisher.publishOutboxEvent(event);
            } catch (Exception e) {
                log.error("[OutboxScheduler] Kafka 발행 재시도 실패 - aggregateId={}, reason={}",
                        event.getAggregateId(), e.getMessage(), e);
            }
        });
    }
}
