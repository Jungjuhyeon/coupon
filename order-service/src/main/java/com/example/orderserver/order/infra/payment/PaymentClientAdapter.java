package com.example.orderserver.order.infra.payment;

import com.example.orderserver.order.application.outputport.PaymentOutputPort;
import com.example.orderserver.order.infra.payment.dto.request.PaymentApproveDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentClientAdapter implements PaymentOutputPort {

    private final PaymentFeignClient paymentFeignClient;

    @Override
    public void save(Long orderId, Integer amount) {
        PaymentApproveDTO request = PaymentApproveDTO.mapToDTO(orderId, amount);
        paymentFeignClient.approve(request);
    }
}
