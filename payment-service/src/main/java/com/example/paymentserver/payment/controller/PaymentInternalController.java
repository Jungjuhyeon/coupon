package com.example.paymentserver.payment.controller;

import com.example.paymentserver.payment.application.usecase.addPaymentUsecase;
import com.example.paymentserver.payment.controller.request.PaymentApproveDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/payments")
public class PaymentInternalController {
    private final addPaymentUsecase addPaymentUsecase;

    @PostMapping("/create")
    public void create(@RequestBody PaymentApproveDTO request) {
        addPaymentUsecase.create(request.getOrderId(), request.getAmount());
    }
}
