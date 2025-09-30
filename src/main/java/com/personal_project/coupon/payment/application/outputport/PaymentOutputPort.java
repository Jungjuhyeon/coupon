package com.personal_project.coupon.payment.application.outputport;

import com.personal_project.coupon.payment.domain.model.Payment;

public interface PaymentOutputPort {

    public void save(Payment payment);
}
