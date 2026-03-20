package com.example.orderserver.order.infra.kafkaadapter;

import com.example.orderserver.order.application.usecase.CompensationUsecase;
import com.example.orderserver.order.domain.model.event.OrderCreatedEventResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedResultConsumer {
    private final ObjectMapper objectMapper;
    private final CompensationUsecase compensationUsecase;

    @KafkaListener(topics = "${kafka.consumer.topic4.name}", groupId = "${kafka.consumer.topic4.groupid}")
    public void consumeOrderCreatedResult(ConsumerRecord<String, String> record){
        OrderCreatedEventResult orderCreatedEventResult = null;
        try {
            orderCreatedEventResult = objectMapper.readValue(record.value(), OrderCreatedEventResult.class);
        } catch (JsonProcessingException e) {
            log.error("역직렬화 실패", e);
            return;
        }

        try{
            if (!orderCreatedEventResult.isSuccess()) {
                compensationUsecase.cancleOrder(orderCreatedEventResult.getOrderId(),orderCreatedEventResult.getMemberId());
            }else{
                compensationUsecase.successOrder(orderCreatedEventResult.getOrderId(),orderCreatedEventResult.getMemberId());
            }
        } catch (Exception e) {
            log.error("주문 결과 처리 실패 orderId={}", orderCreatedEventResult.getOrderId(), e);
        }
    }
}
