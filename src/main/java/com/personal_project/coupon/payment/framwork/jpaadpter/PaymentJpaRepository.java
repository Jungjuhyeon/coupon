package com.personal_project.coupon.payment.framwork.jpaadpter;

import com.personal_project.coupon.payment.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment,Long> {
}
