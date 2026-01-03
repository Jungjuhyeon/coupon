package com.example.orderserver.order.application.outputport;


import com.example.orderserver.order.domain.model.Order;

import java.util.Optional;

public interface OrderOutputPort {

    public Order save(Order order);

    public Optional<Order> findById(Long orderId);

//    public Optional<Order> findByIdMemberId(Long memberId,Long orderId);
}
