package com.example.orderserver.outbox.infra.kafkaadapter;


import com.example.common.global.exception.BusinessException;
import com.example.common.global.exception.errorcode.CommonErrorCode;
import com.example.orderserver.order.domain.model.event.OrderCreatedEvent;
import com.example.orderserver.outbox.application.usecase.OutboxUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedOutboxInternalConsumer {

    private final ObjectMapper objectMapper;
    private final OutboxUseCase outboxUseCase;

    @KafkaListener(topics = "${kafka.consumer.topic3.name}", groupId = "${kafka.consumer.topic3.groupid2}")
    public void ConsumeInternalCreateOutBox(ConsumerRecord<String, String> record){
        log.info("[Outbox] - 내부 리스너 동작");
        String jsonValue = record.value();
        OrderCreatedEvent event = null;
        try {
            event = objectMapper.readValue(jsonValue, OrderCreatedEvent.class);
        } catch (JsonProcessingException e) {
            throw new BusinessException(CommonErrorCode.EVENT_DESERIALIZATION_FAILED);
        }
        //발행성공 상태 변경
        outboxUseCase.markOutboxEventProcessed(event.getOrderId(),event.getEventType());

    }
}
