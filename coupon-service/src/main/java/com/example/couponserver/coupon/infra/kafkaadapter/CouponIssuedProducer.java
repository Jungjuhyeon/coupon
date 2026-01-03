package com.example.couponserver.coupon.infra.kafkaadapter;

import com.example.couponserver.coupon.application.outputport.EventOutputPort;
import com.example.couponserver.coupon.domain.model.event.CouponIssuedEvent;
import com.example.couponserver.coupon.domain.model.event.CouponIssuedLogEvent;
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

    @Value(value = "${kafka.producers.topic1.name}")
    private String TOPIC_ISSUE;

    @Value("${kafka.producers.topic2.name}")
    private String TOPIC_LOG;

    private final KafkaTemplate<String, Object> kafkaTemplate; // Object 하나로 통합

    @Override
    public void occurCouponIssuedEvent(CouponIssuedEvent event) {
        sendMessage(TOPIC_ISSUE, event);
    }

    @Override
    public void occurCouponIssuedLogEvent(CouponIssuedLogEvent event) {
        sendMessage(TOPIC_LOG, event);
    }

    private void sendMessage(String topic, Object event) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, event);

        future.thenAccept(result -> {
            log.info("Sent message=[{}] to topic=[{}] with offset=[{}]",
                    event instanceof CouponIssuedEvent e ? e.getCouponId() : ((CouponIssuedLogEvent) event).getCouponId(),
                    topic,
                    result.getRecordMetadata().offset());
        }).exceptionally(ex -> {
            log.error("Unable to send message=[{}] to topic=[{}] due to: {}",
                    event instanceof CouponIssuedEvent e ? e.getCouponId() : ((CouponIssuedLogEvent) event).getCouponId(),
                    topic,
                    ex.getMessage(), ex);
            return null;
        });
    }


}
