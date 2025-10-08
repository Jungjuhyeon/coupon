package com.personal_project.coupon.order.framwork.jpaadpter;

import com.personal_project.coupon.order.application.outputport.OrderSummaryOutputPort;
import com.personal_project.coupon.order.domain.model.document.OrderSummaryDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSummaryAdapter implements OrderSummaryOutputPort {
    private final OrderSummaryJpaRepository orderSummaryJpaRepository;

    @Override
    public OrderSummaryDocument save(OrderSummaryDocument orderSummaryDocument){
        return orderSummaryJpaRepository.save(orderSummaryDocument);
    }
    @Override
    public List<OrderSummaryDocument> findByMemberId(Long memberId){
        return orderSummaryJpaRepository.findTop20ByMemberIdOrderByOrderTimeDesc(memberId);
    }
}
