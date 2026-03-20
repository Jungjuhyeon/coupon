package com.example.orderserver.order.domain.model.event;

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

    public boolean isSuccess(){
        return this.successed;
    }

}
