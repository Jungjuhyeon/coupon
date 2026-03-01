package com.example.couponserver.coupon.application.scheduler;


import com.example.couponserver.coupon.application.outputport.CouponCacheOutputPort;
import com.example.couponserver.coupon.application.outputport.CouponOutputPort;
import com.example.couponserver.coupon.application.service.CouponIssueEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponIssueRetryScheduler {
    private final CouponOutputPort couponOutputPort;
    private final CouponIssueEventPublisher couponIssueEventPublisher;
    private final CouponCacheOutputPort couponCacheOutputPort;

    // 10초마다 실행
    @Scheduled(fixedDelay = 5000)
    public void retryFailedCouponIssue() {
        LocalDate curDate = LocalDate.now();
        List<Long> activeCouponIds = couponOutputPort.findActiveCoupons(curDate);
        if (activeCouponIds.isEmpty()) return;

        for (Long couponId : activeCouponIds) {
            List<Map.Entry<Object, Object>> entries = couponCacheOutputPort.getReadyToPublishCoupons(couponId);

            for (Map.Entry<Object, Object> entry : entries) {
                Long memberId = Long.parseLong(entry.getKey().toString());
                LocalDateTime issuedAt = LocalDateTime.parse(entry.getValue().toString());

                couponIssueEventPublisher.publishEvent(memberId, couponId, issuedAt);
            }
        }
    }
}
