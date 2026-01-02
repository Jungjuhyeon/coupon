package com.example.couponserver.coupon.infra.kafkaadapter;

import com.example.couponserver.coupon.application.usecase.CouponIssueMakeUsedUseCase;
import com.example.couponserver.coupon.domain.model.event.CouponIssuedEvent;
import com.example.couponserver.coupon.domain.model.event.OrderCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CouponUsedConsumer {
    private final ObjectMapper objectMapper;
    private final CouponIssueMakeUsedUseCase couponIssueMakeUsedUseCase;

    @KafkaListener(
            topics = "${kafka.consumer.topic3.name}",
            groupId = "${kafka.consumer.topic3.groupid1}"
    )
    public void consumeOrderCreated(ConsumerRecord<String,String> record) throws Exception {
        OrderCreatedEvent event =
                objectMapper.readValue(record.value(), OrderCreatedEvent.class);

        if (event.getCouponIssueId() == null) {
            return; // 쿠폰 미사용 주문
        }
        couponIssueMakeUsedUseCase.used(event.getCouponIssueId());

    }

}
