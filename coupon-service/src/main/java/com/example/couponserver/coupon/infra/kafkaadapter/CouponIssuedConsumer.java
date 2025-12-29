package com.example.couponserver.coupon.infra.kafkaadapter;

import com.example.common.global.exception.BusinessException;
import com.example.couponserver.coupon.application.outputport.CouponIssueLogOutputPort;
import com.example.couponserver.coupon.application.outputport.CouponIssueOutputPort;
import com.example.couponserver.coupon.application.outputport.CouponOutputPort;
import com.example.couponserver.coupon.application.outputport.MemberOutputPort;
import com.example.couponserver.coupon.domain.model.Coupon;
import com.example.couponserver.coupon.domain.model.CouponIssue;
import com.example.couponserver.coupon.domain.model.CouponIssueLog;
import com.example.couponserver.coupon.domain.model.event.CouponIssuedEvent;
import com.example.couponserver.coupon.domain.model.event.CouponIssuedLogEvent;
import com.example.couponserver.coupon.exception.CouponErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CouponIssuedConsumer {
    private final ObjectMapper objectMapper;
    private final MemberOutputPort memberOutputPort;
    private final CouponOutputPort couponOutputPort;
    private final CouponIssueOutputPort couponIssueOutputPort;
    private final CouponIssueLogOutputPort couponIssueLogOutputPort;

    private final List<CouponIssueLog> buffer = Collections.synchronizedList(new ArrayList<>());

    @KafkaListener(topics = "${kafka.consumer.topic1.name}", groupId = "${kafka.consumer.topic1.groupid}")
    public void consumeIssue(ConsumerRecord<String,String> record) throws IOException{
        System.out.println("issue:" + record.value());

        CouponIssuedEvent couponIssuedEvent = objectMapper.readValue(record.value(),CouponIssuedEvent.class);

        Long memberId = couponIssuedEvent.getMemberId();
        Long couponId = couponIssuedEvent.getCouponId();

        boolean existsMember = memberOutputPort.existsById(memberId);
        if (!existsMember) {
            throw new BusinessException(CouponErrorCode.PROMOTION_NOT_FOUND); // MEMBER_NOT_FOUND
        }

        Coupon coupon = couponOutputPort.findById(couponId)
                .orElseThrow(() -> new BusinessException(CouponErrorCode.COUPON_NOT_FOUND));

        CouponIssue couponIssue = CouponIssue.create(memberId, coupon, couponIssuedEvent.getCurrentTime());
        couponIssueOutputPort.save(couponIssue);
    }


    @KafkaListener(topics = "${kafka.consumer.topic2.name}", groupId = "${kafka.consumer.topic2.groupid}")
    public void consumeLog(ConsumerRecord<String,String> record) throws IOException {
        CouponIssuedLogEvent event = objectMapper.readValue(record.value(), CouponIssuedLogEvent.class);
        buffer.add(CouponIssueLog.create(event.getMemberId(), event.getCouponId(), event.getEventType()));
    }

    @Scheduled(fixedRate = 10000) // 10초마다 flush
    public void flushLogs() {
        if (!buffer.isEmpty()) {
            List<CouponIssueLog> batch = new ArrayList<>(buffer);
            buffer.clear();
            System.out.println("bulk-issue:");
            couponIssueLogOutputPort.saveAll(batch); // bulk insert
        }
    }
}
