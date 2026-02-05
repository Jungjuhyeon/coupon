package com.example.orderserver.order.application.outputport;

import com.example.orderserver.order.domain.model.event.OrderCreatedEvent;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;


public interface OrderEventOutputPort {

    public CompletableFuture<SendResult<String, Object>> send(OrderCreatedEvent orderCreatedEvent);
}
