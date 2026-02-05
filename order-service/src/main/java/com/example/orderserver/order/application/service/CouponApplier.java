package com.example.orderserver.order.application.service;

import com.example.orderserver.order.application.outputport.CouponOutputPort;
import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.infra.coupon.dto.CouponIssueInfoFeignDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponApplier {
    private final CouponOutputPort couponOutputPort;

    public CouponIssueInfoFeignDTO loadCouponIfExists(Long couponIssueId) {
        if (couponIssueId == null) return null;
        return couponOutputPort.getCouponIssueInfo(couponIssueId);
    }

    public void applyPricing(Order order, CouponIssueInfoFeignDTO couponIssueInfo) {
        if (couponIssueInfo == null) {
            order.finalizePriceWithoutCoupon();
            return;
        }
        order.finalizePriceWithCoupon(couponIssueInfo.getCouponIssueId(), couponIssueInfo.getDiscountValue());
    }
}
