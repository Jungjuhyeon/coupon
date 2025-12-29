package com.example.couponserver.coupon.application.outputport;


import com.example.couponserver.coupon.domain.model.CouponIssue;

import java.util.Optional;

public interface CouponIssueOutputPort {

    CouponIssue save(CouponIssue couponIssue);

    Optional<CouponIssue> findById(Long couponIssueId);

    Optional<CouponIssue> findByIdAndMemberId(Long couponIssueId,Long memberId);
}
