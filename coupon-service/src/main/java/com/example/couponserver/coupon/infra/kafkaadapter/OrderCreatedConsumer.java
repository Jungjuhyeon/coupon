package com.example.couponserver.coupon.infra.kafkaadapter;

import com.example.couponserver.coupon.application.usecase.CouponIssueMakeUsedUseCase;
import com.example.couponserver.coupon.domain.model.event.OrderCreatedEvent;
import com.example.couponserver.coupon.domain.model.event.OrderCreatedEventResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedConsumer {
    private final ObjectMapper objectMapper;
    private final CouponIssueMakeUsedUseCase couponIssueMakeUsedUseCase;
    private final OrderCreatedResultProducer orderCreatedResultProducer;

    @KafkaListener(
            topics = "${kafka.consumer.topic3.name}",
            groupId = "${kafka.consumer.topic3.groupid1}"
    )
    public void consumeOrderCreated(ConsumerRecord<String,String> record) throws Exception {
        OrderCreatedEvent event =
                objectMapper.readValue(record.value(), OrderCreatedEvent.class);

        OrderCreatedEventResult eventResult = OrderCreatedEventResult.create(event.getOrderId(), event.getMemberId(),event.getCouponIssueId(),event.getEventType());

        // ✅ 쿠폰 없는 경우 → 그냥 성공 처리
        if (event.getCouponIssueId() == null) {
            eventResult.success();
            orderCreatedResultProducer.send(eventResult);
            return;
        }

        try {
            couponIssueMakeUsedUseCase.used(event.getCouponIssueId());
            eventResult.success();
        } catch (Exception e) {
            eventResult.fail();
        }
        orderCreatedResultProducer.send(eventResult);
    }

}
