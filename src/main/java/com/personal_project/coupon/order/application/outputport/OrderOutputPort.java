package com.personal_project.coupon.order.application.outputport;

import com.personal_project.coupon.order.domain.model.Order;

public interface OrderOutputPort {

    public Order save(Order order);
}
