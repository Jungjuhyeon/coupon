package com.personal_project.coupon.order.framwork.jpaadpter;

import com.personal_project.coupon.order.application.outputport.OrderOutputPort;
import com.personal_project.coupon.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderAdapter implements OrderOutputPort {
    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order){
        return orderJpaRepository.save(order);
    }

}
