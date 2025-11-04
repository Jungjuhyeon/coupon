package com.personal_project.coupon.order.application.outputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal_project.coupon.order.domain.model.event.OrderCreatedEvent;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;


public interface OrderEventOutputPort {

    public CompletableFuture<SendResult<String, Object>> send(OrderCreatedEvent orderCreatedEvent)throws JsonProcessingException;
}
