package com.personal_project.coupon.coupon.application.outputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal_project.coupon.coupon.domain.model.event.CouponIssuedEvent;
import com.personal_project.coupon.coupon.domain.model.event.CouponIssuedLogEvent;

public interface EventOutputPort {
    public void occurCouponIssuedEvent(CouponIssuedEvent couponIssuedEvent)throws JsonProcessingException;

    public void occurCouponIssuedLogEvent(CouponIssuedLogEvent couponIssuedLogEvent)throws JsonProcessingException;

}
