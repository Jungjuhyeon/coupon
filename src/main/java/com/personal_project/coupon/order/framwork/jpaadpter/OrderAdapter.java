package com.personal_project.coupon.order.framwork.jpaadpter;

import com.personal_project.coupon.order.application.outputport.OrderOutputPort;
import com.personal_project.coupon.order.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderAdapter implements OrderOutputPort {
    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order){
        return orderJpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long orderId){
        return orderJpaRepository.findById(orderId);
    }


    @Override
    public Optional<Order> findOrderDetail(Long memberId,Long orderId){
        return orderJpaRepository.findOrderDetail(memberId,orderId);
    }

    @Override
    public List<Order> findOrderList(Long memberId){
        return orderJpaRepository.findOrderList(memberId);
    }

    @Override
    public Optional<Order> findByIdMemberId(Long orderId,Long memberId){
        return orderJpaRepository.findOrder(memberId,orderId);
    }


}
