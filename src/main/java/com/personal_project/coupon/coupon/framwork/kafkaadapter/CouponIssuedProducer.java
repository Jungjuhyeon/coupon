package com.personal_project.coupon.coupon.framwork.kafkaadapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal_project.coupon.coupon.application.outputport.EventOutputPort;
import com.personal_project.coupon.coupon.domain.model.event.CouponIssuedEvent;
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
public class CouponIssuedProducer implements EventOutputPort {

    @Value(value = "${producers.topic1.name}")
    private String TOPIC_ISSUE;

    private final KafkaTemplate<String, CouponIssuedEvent> kafkaTemplate;
    @Override
    public void occurCouponIssuedEvent(CouponIssuedEvent couponIssuedEvent) throws JsonProcessingException {
        CompletableFuture<SendResult<String, CouponIssuedEvent>> future = kafkaTemplate.send(TOPIC_ISSUE, couponIssuedEvent);

        future.thenAccept(result -> {
            CouponIssuedEvent g = result.getProducerRecord().value();
            log.info("Sent message=[{}] with offset=[{}]",
                    g.getCouponId(), result.getRecordMetadata().offset());
        }).exceptionally(ex -> {
            log.error("Unable to send message=[{}] due to: {}",
                    couponIssuedEvent.getCouponId(), ex.getMessage(), ex);
            return null;
        });
    }
}
