package com.example.paymentserver.payment.application.inputport;

import com.example.paymentserver.payment.application.outputport.PaymentOutputPort;
import com.example.paymentserver.payment.domain.model.Payment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class addPaymentInputPortTest {

    @InjectMocks
    private addPaymentInputPort addPaymentInputPort;

    @Mock
    private PaymentOutputPort paymentOutputPort;

    @Test
    @DisplayName("결제 생성 성공 - Payment 저장 호출 검증")
    void create_success() {
        // given
        Long orderId = 1L;
        Integer amount = 20000;

        // when
        addPaymentInputPort.create(orderId, amount);

        // then
        verify(paymentOutputPort).save(any(Payment.class));
    }
}
