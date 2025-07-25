package com.personal_project.coupon.coupon.application.outputport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal_project.coupon.coupon.domain.model.event.CouponIssuedEvent;

public interface EventOutputPort {
    public void occurCouponIssuedEvent(CouponIssuedEvent couponIssuedEvent)throws JsonProcessingException;
}
