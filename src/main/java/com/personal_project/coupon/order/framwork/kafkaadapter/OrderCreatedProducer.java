package com.personal_project.coupon.order.framwork.kafkaadapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal_project.coupon.order.application.outputport.OrderEventOutputPort;
import com.personal_project.coupon.order.domain.model.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedProducer implements OrderEventOutputPort {
    @Value(value = "${kafka.producers.topic3.name}")
    private String TOPIC;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void occurOrderEvent(OrderCreatedEvent result)throws JsonProcessingException {

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC, result);

        future.thenAccept(sendResult -> {
            OrderCreatedEvent sentResult = (OrderCreatedEvent) sendResult.getProducerRecord().value();
            log.info("Sent message=[{}] with offset=[{}]",
                    sentResult.getOrderId(), sendResult.getRecordMetadata().offset());
        }).exceptionally(ex -> {
            log.error("Unable to send message=[{}] due to: {}",
                    result.getOrderId(), ex.getMessage(), ex);
            return null;
        });
    }

}
