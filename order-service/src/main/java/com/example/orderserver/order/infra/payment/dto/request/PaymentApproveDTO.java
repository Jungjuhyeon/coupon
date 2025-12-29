package com.example.orderserver.order.infra.payment.dto.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentApproveDTO {
    private Long orderId;
    private Integer amount;
    public static PaymentApproveDTO mapToDTO(Long orderId, Integer amount) {
        return PaymentApproveDTO.builder()
                .orderId(orderId)
                .amount(amount)
                .build();
    }
}
