package com.personal_project.coupon.coupon.application.outputport;

import com.personal_project.coupon.coupon.domain.model.CouponIssue;

public interface CouponIssueOutputPort {

    CouponIssue save(CouponIssue couponIssue);
}
