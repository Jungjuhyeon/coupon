package com.example.orderserver.order.infra.payment;

import com.example.orderserver.order.infra.payment.dto.request.PaymentApproveDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service", url = "${payment.service.url}")
public interface PaymentFeignClient {

    @PostMapping("/internal/payments/create")
    void approve(@RequestBody PaymentApproveDTO request);
}
