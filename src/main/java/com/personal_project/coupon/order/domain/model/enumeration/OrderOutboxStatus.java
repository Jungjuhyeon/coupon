package com.personal_project.coupon.order.domain.model.enumeration;

public enum OrderOutboxStatus {
    READY_TO_PUBLISH,
    PUBLISHED,
    MESSAGE_CONSUME,
    FAILED
}
