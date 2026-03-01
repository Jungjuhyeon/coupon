package com.example.couponserver.coupon.application.outputport;

import com.example.couponserver.coupon.domain.model.event.CouponIssuedEvent;
import com.example.couponserver.coupon.domain.model.event.CouponIssuedLogEvent;

import java.util.concurrent.CompletableFuture;

public interface EventOutputPort {
    CompletableFuture<?> occurCouponIssuedEvent(CouponIssuedEvent couponIssuedEvent);

    void occurCouponIssuedLogEvent(CouponIssuedLogEvent couponIssuedLogEvent);

}
