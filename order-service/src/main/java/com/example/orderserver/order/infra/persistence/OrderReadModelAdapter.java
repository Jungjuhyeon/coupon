package com.example.orderserver.order.infra.persistence;

import com.example.orderserver.order.application.outputport.OrderReadModelOutputPort;
import com.example.orderserver.order.domain.model.document.OrderReadModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderReadModelAdapter implements OrderReadModelOutputPort {
    private final OrderReadModelJpaRepository orderReadModelJpaRepository;

    @Override
    public OrderReadModel save(OrderReadModel orderReadModel){
        return orderReadModelJpaRepository.save(orderReadModel);
    }
    @Override
    public List<OrderReadModel> findByMemberId(Long memberId){
        return orderReadModelJpaRepository.findTop20ByMemberIdOrderByOrderTimeDesc(memberId);
    }
}
