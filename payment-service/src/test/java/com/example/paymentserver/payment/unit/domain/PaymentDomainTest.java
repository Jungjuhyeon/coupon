package com.example.paymentserver.payment.unit.domain;

import com.example.paymentserver.payment.domain.model.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentDomainTest {

    @Test
    @DisplayName("Payment.create() 시 orderId와 amount가 정확히 저장된다")
    void create_stores_orderId_and_amount() {
        // when
        Payment payment = Payment.create(1L, 20000);

        // then
        assertThat(payment.getOrderId()).isEqualTo(1L);
        assertThat(payment.getAmount()).isEqualTo(20000);
    }
}
