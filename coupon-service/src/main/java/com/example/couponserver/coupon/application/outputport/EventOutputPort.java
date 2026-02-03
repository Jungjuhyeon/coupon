package com.example.couponserver.coupon.application.outputport;

import com.example.couponserver.coupon.domain.model.event.CouponIssuedEvent;
import com.example.couponserver.coupon.domain.model.event.CouponIssuedLogEvent;

public interface EventOutputPort {
    void occurCouponIssuedEvent(CouponIssuedEvent couponIssuedEvent);

    void occurCouponIssuedLogEvent(CouponIssuedLogEvent couponIssuedLogEvent);

}
