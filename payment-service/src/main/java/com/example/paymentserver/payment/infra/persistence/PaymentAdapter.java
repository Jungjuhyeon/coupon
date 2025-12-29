package com.example.paymentserver.payment.infra.persistence;

import com.example.paymentserver.payment.application.outputport.PaymentOutputPort;
import com.example.paymentserver.payment.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentAdapter implements PaymentOutputPort {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public void save(Payment payment){
        paymentJpaRepository.save(payment);
    }

    @Override
    public Optional<Payment> findByOrderId(Long paymentId){
       return paymentJpaRepository.findByOrderId(paymentId);
    }

}
