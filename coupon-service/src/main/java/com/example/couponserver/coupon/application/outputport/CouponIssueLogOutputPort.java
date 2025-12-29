package com.example.couponserver.coupon.application.outputport;


import com.example.couponserver.coupon.domain.model.CouponIssueLog;

import java.util.List;

public interface CouponIssueLogOutputPort {

    void save(CouponIssueLog couponIssueLog);
    void saveAll(List<CouponIssueLog> batch);
}
