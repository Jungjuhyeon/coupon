package com.example.couponserver.coupon.application.usecase;

import com.example.couponserver.coupon.framework.web.response.CouponIssueOrderFeignDTO;

public interface InquiryCouponIssueUseCase {
    CouponIssueOrderFeignDTO getCouponIssueValue(Long couponIssueId);
}
