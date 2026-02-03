package com.example.couponserver.coupon.application.usecase;

import com.example.couponserver.coupon.domain.model.Coupon;

import java.time.LocalDateTime;

public interface AddCouponIssueUseCase {
    void addCouponIssue(Long memberId, Coupon coupon, LocalDateTime currentTime);
}
