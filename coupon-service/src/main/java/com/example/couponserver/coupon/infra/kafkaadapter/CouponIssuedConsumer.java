package com.example.couponserver.coupon.infra.kafkaadapter;

import com.example.common.global.exception.BusinessException;
import com.example.couponserver.coupon.application.outputport.MemberOutputPort;
import com.example.couponserver.coupon.application.usecase.AddCouponIssueLogUseCase;
import com.example.couponserver.coupon.application.usecase.AddCouponIssueUseCase;
import com.example.couponserver.coupon.application.usecase.InquiryCouponUseCase;
import com.example.couponserver.coupon.domain.model.Coupon;
import com.example.couponserver.coupon.domain.model.event.CouponIssuedEvent;
import com.example.couponserver.coupon.domain.model.event.CouponIssuedLogEvent;
import com.example.couponserver.coupon.exception.CouponErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponIssuedConsumer {
    private final ObjectMapper objectMapper;
    private final InquiryCouponUseCase inquiryCouponUseCase;
    private final AddCouponIssueUseCase addCouponIssueUseCase;
    private final MemberOutputPort memberOutputPort;
    private final AddCouponIssueLogUseCase addCouponIssueLogUseCase;

    @KafkaListener(topics = "${kafka.consumer.topic1.name}", groupId = "${kafka.consumer.topic1.groupid}")
    @RetryableTopic(
            // 총 시도 횟수 (최초 시도 1회 + 재시도 4회)
            attempts = "5",
            // 재시도 간격 (1000ms -> 2000ms -> 4000ms -> 8000ms 순으로 재시도 시간이 증가)
            backoff = @Backoff(delay = 1000, multiplier = 2),
            dltTopicSuffix = ".dlt"
    )
    public void consumeIssue(ConsumerRecord<String,String> record) throws IOException{
        CouponIssuedEvent couponIssuedEvent = objectMapper.readValue(record.value(),CouponIssuedEvent.class);

        Long memberId = couponIssuedEvent.getMemberId();
        Long couponId = couponIssuedEvent.getCouponId();

        if (!memberOutputPort.existsById(memberId)) {
            throw new BusinessException(CouponErrorCode.MEMBER_NOT_FOUND);
        }
        Coupon coupon = inquiryCouponUseCase.getCouponById(couponId);
        try {
            addCouponIssueUseCase.addCouponIssue(memberId, coupon, couponIssuedEvent.getCurrentTime());
        } catch (DuplicateKeyException e) {
            log.warn("[중복 발급 무시] memberId={}, couponId={}", memberId, couponId);
        }
    }


    @KafkaListener(topics = "${kafka.consumer.topic2.name}", groupId = "${kafka.consumer.topic2.groupid}")
    public void consumeLog(ConsumerRecord<String,String> record) throws IOException {
        CouponIssuedLogEvent event = objectMapper.readValue(record.value(), CouponIssuedLogEvent.class);
        addCouponIssueLogUseCase.addCouponIssue(event.getMemberId(), event.getCouponId(), event.getEventType());
    }

    @Scheduled(fixedRate = 10000) // 10초마다
    public void flushLogs() {
        addCouponIssueLogUseCase.flush();
    }
}
