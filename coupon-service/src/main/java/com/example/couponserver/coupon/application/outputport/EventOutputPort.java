package com.example.couponserver.coupon.application.outputport;

import com.example.couponserver.coupon.domain.model.event.CouponIssuedEvent;
import com.example.couponserver.coupon.domain.model.event.CouponIssuedLogEvent;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface EventOutputPort {
    public void occurCouponIssuedEvent(CouponIssuedEvent couponIssuedEvent)throws JsonProcessingException;

    public void occurCouponIssuedLogEvent(CouponIssuedLogEvent couponIssuedLogEvent)throws JsonProcessingException;

}
