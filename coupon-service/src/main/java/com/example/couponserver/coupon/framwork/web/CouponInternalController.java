package com.example.couponserver.coupon.framwork.web;


import com.example.couponserver.coupon.application.usecase.InquiryCouponIssueUseCase;
import com.example.couponserver.coupon.framwork.web.response.CouponIssueOrderFeignDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class CouponInternalController {
    private final InquiryCouponIssueUseCase inquiryCouponIssueUseCase;

    @GetMapping("/coupon-issues/{couponIssueId}")
    public CouponIssueOrderFeignDTO getCouponIssueValue(@PathVariable Long couponIssueId){
        return inquiryCouponIssueUseCase.getCouponIssueValue(couponIssueId);
    }


}

