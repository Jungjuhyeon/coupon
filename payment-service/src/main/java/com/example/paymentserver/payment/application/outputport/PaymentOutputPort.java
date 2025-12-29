package com.example.paymentserver.payment.application.outputport;


import com.example.paymentserver.payment.domain.model.Payment;

import java.util.Optional;

public interface PaymentOutputPort {

    public void save(Payment payment);
    public Optional<Payment> findByOrderId(Long paymentId);
}

