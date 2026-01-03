package com.example.paymentserver.payment.infra.persistence;

import com.example.paymentserver.payment.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<Payment,Long> {

    Optional<Payment> findByOrderId(Long orderId);
}
