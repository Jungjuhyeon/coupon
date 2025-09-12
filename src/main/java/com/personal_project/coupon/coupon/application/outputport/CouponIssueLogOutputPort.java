package com.personal_project.coupon.coupon.application.outputport;

import com.personal_project.coupon.coupon.domain.model.CouponIssueLog;

import java.util.List;

public interface CouponIssueLogOutputPort {

    void save(CouponIssueLog couponIssueLog);
    void saveAll(List<CouponIssueLog> batch);
}
