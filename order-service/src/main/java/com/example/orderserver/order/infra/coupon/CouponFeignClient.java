package com.example.orderserver.order.infra.coupon;

import com.example.orderserver.order.infra.coupon.dto.CouponIssueInfoFeignDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "COUPONSERVER")
public interface CouponFeignClient {

    @GetMapping("/internal/coupon-issues/{couponIssueId}")
    CouponIssueInfoFeignDTO getCouponIssue(@PathVariable("couponIssueId") Long couponIssueId);

}