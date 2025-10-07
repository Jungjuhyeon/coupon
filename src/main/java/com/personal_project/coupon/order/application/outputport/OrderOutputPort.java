package com.personal_project.coupon.order.application.outputport;

import com.personal_project.coupon.order.domain.model.Order;

import java.util.Optional;

public interface OrderOutputPort {

    public Order save(Order order);

    public Optional<Order> findById(Long orderId);

    public Optional<Order> findOrderDetail(Long memberId,Long orderId);
}
