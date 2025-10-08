package com.personal_project.coupon.payment.application.outputport;

import com.personal_project.coupon.payment.domain.model.Payment;

import java.util.Optional;

public interface PaymentOutputPort {

    public void save(Payment payment);
    public Optional<Payment> findByOrderId(Long paymentId);
}

