package com.example.orderserver.order.application.outputport;

public interface PaymentOutputPort {
    void save(Long orderId, Integer amount);
}
