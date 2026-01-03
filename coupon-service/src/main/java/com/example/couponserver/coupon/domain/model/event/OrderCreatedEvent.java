package com.example.couponserver.coupon.domain.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private Long memberId;
    private Long couponIssueId;
    private String eventType;
}
