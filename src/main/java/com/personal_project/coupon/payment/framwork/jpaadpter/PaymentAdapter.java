package com.personal_project.coupon.payment.framwork.jpaadpter;

import com.personal_project.coupon.payment.application.outputport.PaymentOutputPort;
import com.personal_project.coupon.payment.domain.model.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentAdapter implements PaymentOutputPort {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public void save(Payment payment){
        paymentJpaRepository.save(payment);
    }
}
