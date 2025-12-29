package com.example.orderserver.order.infra.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponIssueInfoFeignDTO {
    Long couponIssueId;
    int discountValue;
}
