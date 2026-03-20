package com.example.couponserver.coupon.domain.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEventResult {
    private Long orderId;
    private Long memberId;
    private Long couponIssueId;
    private String eventType;
    private boolean successed;

    public static OrderCreatedEventResult create(Long orderId, Long memberId,Long couponIssueId, String eventType){
        OrderCreatedEventResult result = new OrderCreatedEventResult();
        result.orderId = orderId;
        result.memberId = memberId;
        result.eventType = eventType;
        result.couponIssueId = couponIssueId;
        return result;
    }
    public OrderCreatedEventResult success(){
        this.successed = true;
        return this;
    }

    public OrderCreatedEventResult fail(){
        this.successed = false;
        return this;
    }
}
