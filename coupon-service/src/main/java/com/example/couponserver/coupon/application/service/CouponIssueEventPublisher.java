package com.example.couponserver.coupon.application.service;

import com.example.couponserver.coupon.application.outputport.CouponCacheOutputPort;
import com.example.couponserver.coupon.application.outputport.EventOutputPort;
import com.example.couponserver.coupon.domain.model.CouponIssue;
import com.example.couponserver.coupon.domain.model.CouponIssueLog;
import com.example.couponserver.coupon.domain.model.event.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponIssueEventPublisher {
    private final EventOutputPort eventOutputPort;
    private final CouponCacheOutputPort couponCacheOutputPort;

    public void publishEvent(Long memberId, Long couponId, LocalDateTime now){
        eventOutputPort.occurCouponIssuedEvent(
                CouponIssue.createCouponIssueEvent(couponId, memberId, now)
        ).thenAccept(result -> {
            couponCacheOutputPort.remove("coupon:{" + couponId + "}:ready_to_publish", memberId.toString());
        });
    }

    public void publishSuccessLog(Long memberId, Long couponId, LocalDateTime now) {
        publishLog(memberId, couponId, now, EventType.SUCCESS);
    }

    public void publishFailLog(Long memberId, Long couponId, LocalDateTime now, EventType type) {
        publishLog(memberId, couponId, now, type);
    }

    private void publishLog(Long memberId, Long couponId, LocalDateTime now, EventType type){
        eventOutputPort.occurCouponIssuedLogEvent(
                CouponIssueLog.createCouponIssuedLogEvent(memberId, couponId, now, type)
        );
    }
}
