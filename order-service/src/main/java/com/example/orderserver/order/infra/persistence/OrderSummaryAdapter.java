package com.example.orderserver.order.infra.persistence;

import com.example.orderserver.order.application.outputport.OrderSummaryOutputPort;
import com.example.orderserver.order.domain.model.document.OrderSummaryDocument;
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
