package com.personal_project.coupon.order.framwork.kafkaadapter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.personal_project.coupon.order.application.usecase.OrderOutboxUseCase;
import com.personal_project.coupon.order.domain.model.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedOutboxInternalConsumer {

    private final ObjectMapper objectMapper;
    private final OrderOutboxUseCase orderOutboxUseCase;


    @KafkaListener(topics = "${kafka.consumer.topic3.name}", groupId = "${kafka.consumer.topic3.groupid2}")
    public void ConsumeInternalCreateOutBox(ConsumerRecord<String, String> record) throws IOException {
        log.info("[Outbox] - 내부 리스너 동작");
        String jsonValue = record.value();
        OrderCreatedEvent event = objectMapper.readValue(jsonValue, OrderCreatedEvent.class);

        //발행성공 상태 변경
        orderOutboxUseCase.markOutboxEventProcessed(event);

    }
}
