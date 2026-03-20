package com.example.couponserver.coupon.infra.kafkaadapter;

import com.example.couponserver.coupon.domain.model.event.OrderCreatedEventResult;
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
public class OrderCreatedResultProducer {
    @Value(value = "${kafka.producers.topic3.name}")
    private String TOPIC;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public CompletableFuture<SendResult<String, Object>> send(Object event) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(TOPIC, event);

        future.thenAccept(sendResult -> {
            OrderCreatedEventResult sentResult = (OrderCreatedEventResult) sendResult.getProducerRecord().value();
            log.info("Sent message=[{}] with offset=[{}]",
                    sentResult.getOrderId(), sendResult.getRecordMetadata().offset());
        }).exceptionally(ex -> {
            return null;
        });
        return future;
    }
}
