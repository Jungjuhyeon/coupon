package com.example.couponserver.coupon.application.usecase;

import com.example.couponserver.coupon.framwork.web.response.CouponIssueOrderFeignDTO;

public interface InquiryCouponIssueUseCase {
    CouponIssueOrderFeignDTO getCouponIssueValue(Long couponIssueId);
}
