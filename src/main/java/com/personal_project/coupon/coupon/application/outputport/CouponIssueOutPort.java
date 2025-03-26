package com.personal_project.coupon.coupon.application.outputport;

import com.personal_project.coupon.coupon.domain.CouponIssue;

public interface CouponIssueOutPort {
    boolean existByUserIdAndCouponId(Long memberId,Long couponId);

    CouponIssue save(CouponIssue couponIssue);
}
