package com.example.paymentserver.payment.application.inputport;

import com.example.paymentserver.payment.application.outputport.PaymentOutputPort;
import com.example.paymentserver.payment.application.usecase.addPaymentUsecase;
import com.example.paymentserver.payment.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class addPaymentInputPort implements addPaymentUsecase {

    private final PaymentOutputPort paymentOutputPort;
    @Override
    public void create(Long orderId, Integer amount) {
        Payment payment = Payment.create(orderId, amount);
        paymentOutputPort.save(payment);
    }
}
