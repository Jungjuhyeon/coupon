package com.personal_project.coupon.payment.framwork.jpaadpter;

import com.personal_project.coupon.payment.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentJpaRepository extends JpaRepository<Payment,Long> {

    Optional<Payment> findByOrderId(Long orderId);
}
