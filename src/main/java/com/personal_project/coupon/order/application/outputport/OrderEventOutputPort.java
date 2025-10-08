package com.personal_project.coupon.order.application.outputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal_project.coupon.order.domain.model.event.OrderCreatedEvent;

public interface OrderEventOutputPort {

    public void occurOrderEvent(OrderCreatedEvent orderCreatedEvent)throws JsonProcessingException;
}
