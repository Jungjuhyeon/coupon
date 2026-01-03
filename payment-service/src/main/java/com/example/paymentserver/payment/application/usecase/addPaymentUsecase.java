package com.example.paymentserver.payment.application.usecase;

public interface addPaymentUsecase {
    void create(Long orderId, Integer amount);
}
