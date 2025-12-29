package com.example.couponserver.coupon.domain.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponIssuedLogEvent {
    private Long couponId;
    private Long memberId;
    private LocalDateTime issueDateTime;
    private EventType eventType;
}
