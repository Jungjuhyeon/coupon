package com.example.paymentserver.payment.controller.request;

import lombok.Getter;

@Getter
public class PaymentApproveDTO {
    private Long orderId;
    private Integer amount;
}
