package com.personal_project.coupon.order.outbox.application.inputport;

import com.personal_project.coupon.order.outbox.application.outputport.OutboxOutputPort;
import com.personal_project.coupon.order.outbox.application.usecase.OutboxUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OutboxInputPort implements OutboxUseCase {

    private final OutboxOutputPort outboxOutputPort;
    @Override
    public void markOutboxEventProcessed(Long orderId ,String eventType){
        outboxOutputPort.findByAggregateIdAndEventType(orderId,eventType)
                .ifPresent(outbox -> {
                            outbox.markOutboxEventProcessed();
                            outboxOutputPort.save(outbox);
                });
    }

}
