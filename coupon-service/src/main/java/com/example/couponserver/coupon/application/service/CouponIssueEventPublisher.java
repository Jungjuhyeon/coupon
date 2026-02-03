package com.example.couponserver.coupon.application.service;

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

    public void publishSuccess(Long memberId, Long couponId, LocalDateTime now){
        eventOutputPort.occurCouponIssuedEvent(
                CouponIssue.createCouponIssueEvent(couponId, memberId, now)
        );
        publishLog(memberId, couponId, now, EventType.SUCCESS);
    }

    public void publishFail(Long memberId, Long couponId, LocalDateTime now, EventType type) {
        publishLog(memberId, couponId, now, type);
    }

    private void publishLog(Long memberId, Long couponId, LocalDateTime now, EventType type){
        eventOutputPort.occurCouponIssuedLogEvent(
                CouponIssueLog.createCouponIssuedLogEvent(memberId, couponId, now, type)
        );
    }
}
