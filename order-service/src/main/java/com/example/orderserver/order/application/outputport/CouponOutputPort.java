package com.example.orderserver.order.application.outputport;

import com.example.orderserver.order.infra.coupon.dto.CouponIssueInfoFeignDTO;

public interface CouponOutputPort {
    CouponIssueInfoFeignDTO getCouponIssueInfo(Long couponIssueId);
}
