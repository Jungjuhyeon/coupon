package com.example.orderserver.order.infra.kafkaadapter;

import com.example.common.global.exception.BusinessException;
import com.example.orderserver.order.application.outputport.OrderOutputPort;
import com.example.orderserver.order.application.outputport.OrderSummaryOutputPort;
import com.example.orderserver.order.application.outputport.StoreOutputPort;
import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.domain.model.OrderMenu;
import com.example.orderserver.order.domain.model.document.OrderSummaryDocument;
import com.example.orderserver.order.domain.model.event.OrderCreatedEvent;
import com.example.orderserver.order.exception.OrderErrorCode;
import com.example.orderserver.order.infra.assembler.OrderSummaryAssembler;
import com.example.orderserver.order.infra.store.dto.response.StoreOrderViewFeignDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderCreatedConsumer {

    private final OrderOutputPort orderOutputPort;
    private final OrderSummaryOutputPort orderSummaryOutputPort;
    private final ObjectMapper objectMapper;
    private final StoreOutputPort storeOutputPort;

    private final OrderSummaryAssembler orderSummaryAssembler;
    @KafkaListener(topics = "${kafka.consumer.topic3.name}", groupId = "${kafka.consumer.topic3.groupid1}")
    @RetryableTopic(
            // 총 시도 횟수 (최초 시도 1회 + 재시도 4회)
            attempts = "5",
            // 재시도 간격 (1000ms -> 2000ms -> 4000ms -> 8000ms 순으로 재시도 시간이 증가한다.)
            backoff = @Backoff(delay = 1000, multiplier = 2),
            dltTopicSuffix = ".dlt"
    )
    public void consumeOrderCreated(ConsumerRecord<String, String> record) throws IOException {
        log.info("issue:" + record.value());

        OrderCreatedEvent orderCreatedEvent = objectMapper.readValue(record.value(),OrderCreatedEvent.class);

        Order order = orderOutputPort.findById(orderCreatedEvent.getOrderId())
                .orElseThrow(()-> new BusinessException(OrderErrorCode.ORDER_NOT_FOUND));

        StoreOrderViewFeignDTO storeOrderView =
                storeOutputPort.getStoreOrderViewForProjection(order.getStoreId(), order.getOrderMenuList().stream()
                        .map(OrderMenu::getMenuId).toList());

        OrderSummaryDocument document =
                orderSummaryAssembler.assemble(order, orderCreatedEvent.getMemberId(), storeOrderView);

        try {
            orderSummaryOutputPort.save(document);
        }catch (DuplicateKeyException e) {
            log.warn(" 중복 이벤트 감지 - 저장 생략 ");
        }
    }
}
