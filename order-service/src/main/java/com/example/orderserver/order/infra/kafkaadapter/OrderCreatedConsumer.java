package com.example.orderserver.order.infra.kafkaadapter;

import com.example.common.global.exception.BusinessException;
import com.example.common.global.exception.errorcode.CommonErrorCode;
import com.example.orderserver.order.application.outputport.StoreOutputPort;
import com.example.orderserver.order.application.usecase.AddOrderReadModelUseCase;
import com.example.orderserver.order.application.usecase.InquiryOrderUseCase;
import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.domain.model.OrderMenu;
import com.example.orderserver.order.domain.model.document.OrderReadModel;
import com.example.orderserver.order.domain.model.event.OrderCreatedEvent;
import com.example.orderserver.order.infra.assembler.OrderReadModelAssembler;
import com.example.orderserver.order.infra.store.dto.response.StoreOrderViewFeignDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderCreatedConsumer {

    private final ObjectMapper objectMapper;
    private final OrderReadModelAssembler orderReadModelAssembler;
    private final InquiryOrderUseCase inquiryOrderUseCase;
    private final StoreOutputPort storeOutputPort;
    private final AddOrderReadModelUseCase addOrderReadModelUseCase;

    @KafkaListener(topics = "${kafka.consumer.topic3.name}", groupId = "${kafka.consumer.topic3.groupid1}")
    @RetryableTopic(
            // 총 시도 횟수 (최초 시도 1회 + 재시도 4회)
            attempts = "5",
            // 재시도 간격 (1000ms -> 2000ms -> 4000ms -> 8000ms 순으로 재시도 시간이 증가한다.)
            backoff = @Backoff(delay = 1000, multiplier = 2),
            dltTopicSuffix = ".dlt"
    )
    public void consumeOrderCreated(ConsumerRecord<String, String> record){
        OrderCreatedEvent orderCreatedEvent = null;
        try {
            orderCreatedEvent = objectMapper.readValue(record.value(), OrderCreatedEvent.class);
        } catch (JsonProcessingException e) {
            throw new BusinessException(CommonErrorCode.EVENT_DESERIALIZATION_FAILED);
        }

        Order order = inquiryOrderUseCase.getOrderById(orderCreatedEvent.getOrderId());

        StoreOrderViewFeignDTO storeOrderView =
                storeOutputPort.getRequiredStoreOrderView(order.getStoreId(), order.getOrderMenuList().stream()
                        .map(OrderMenu::getMenuId).toList());

        OrderReadModel document =
                orderReadModelAssembler.assemble(order, orderCreatedEvent.getMemberId(), storeOrderView);

        try {
            addOrderReadModelUseCase.addOrderReadModel(document);
        }catch (DataIntegrityViolationException e) {
            log.warn(" 중복 이벤트 감지 - 저장 생략 ");
        }
    }
}
