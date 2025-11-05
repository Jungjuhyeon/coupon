package com.personal_project.coupon.order.framwork.kafkaadapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.DuplicateKeyException;
import com.personal_project.coupon.coupon.domain.model.enumeration.DiscountType;
import com.personal_project.coupon.global.exception.BusinessException;
import com.personal_project.coupon.global.exception.errorcode.CommonErrorCode;
import com.personal_project.coupon.order.application.outputport.OrderOutputPort;
import com.personal_project.coupon.order.application.outputport.OrderSummaryOutputPort;
import com.personal_project.coupon.order.domain.model.Order;
import com.personal_project.coupon.order.domain.model.document.OrderSummaryDocument;
import com.personal_project.coupon.order.domain.model.event.OrderCreatedEvent;
import com.personal_project.coupon.store.domain.model.Brand;
import com.personal_project.coupon.store.domain.model.Store;
import com.personal_project.coupon.store.domain.model.StoreCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderCreatedConsumer {

    private final OrderOutputPort orderOutputPort;
    private final OrderSummaryOutputPort orderSummaryOutputPort;
    private final ObjectMapper objectMapper;


    @KafkaListener(topics = "${kafka.consumer.topic3.name}", groupId = "${kafka.consumer.topic3.groupid1}")
    public void consumeOrderCreated(ConsumerRecord<String, String> record) throws IOException {
        log.info("issue:" + record.value());

        OrderCreatedEvent orderCreatedEvent = objectMapper.readValue(record.value(),OrderCreatedEvent.class);


        Order order = orderOutputPort.findByIdMemberId(orderCreatedEvent.getOrderId(),orderCreatedEvent.getMemberId())
                .orElseThrow(()-> new BusinessException(CommonErrorCode.ORDER_NOT_FOUND));

        Store store = order.getStore();
        StoreCategory storeCategory = store.getStoreCategory();
        Brand brand = store.getBrand();

        DiscountType discountType = Optional.ofNullable(order.getCouponIssue())
                .map(ci -> ci.getCoupon().getDiscountType())
                .orElse(null); // 또는 기본값 지정

        OrderSummaryDocument orderSummaryDocument =
                OrderSummaryDocument.fromEvent(order,orderCreatedEvent.getMemberId(),
                        storeCategory.getName(),
                        brand.getName(),store.getName(), discountType);

        try {
            orderSummaryOutputPort.save(orderSummaryDocument);
        }catch (DuplicateKeyException e) {
            log.warn(" 중복 이벤트 감지 - 저장 생략 ");
        }
    }
}
