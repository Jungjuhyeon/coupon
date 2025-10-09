package com.personal_project.coupon.order.application.inputport;

import com.personal_project.coupon.order.application.outputport.OrderOutboxOutputPort;
import com.personal_project.coupon.order.application.usecase.OrderOutboxUseCase;
import com.personal_project.coupon.order.domain.model.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class OrderOutboxInputPort implements OrderOutboxUseCase {

    private final OrderOutboxOutputPort orderOutboxOutputPort;
    @Override
    public void markOutboxEventProcessed(OrderCreatedEvent event){
        orderOutboxOutputPort.findByAggregateIdAndEventType(event.getOrderId(),event.getEventType())
                .ifPresent(outbox -> {
                            outbox.markOutboxEventProcessed();
                            orderOutboxOutputPort.save(outbox);

                });
    }

}
