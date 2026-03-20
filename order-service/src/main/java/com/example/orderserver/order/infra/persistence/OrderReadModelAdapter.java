package com.example.orderserver.order.infra.persistence;

import com.example.orderserver.order.application.outputport.OrderReadModelOutputPort;
import com.example.orderserver.order.domain.model.document.OrderReadModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderReadModelAdapter implements OrderReadModelOutputPort {
    private final OrderReadModelJpaRepository orderReadModelJpaRepository;

    @Override
    public List<OrderReadModel> findByMemberId(Long memberId){
        return orderReadModelJpaRepository.findTop20ByMemberIdOrderByOrderTimeDesc(memberId);
    }

    @Override
    public Optional<OrderReadModel> findByOrderId(Long orderId){
        return orderReadModelJpaRepository.findByOrderId(orderId);
    }

    @Override
    public void upsertOrderCreated(OrderReadModel orderReadModel) {
        orderReadModelJpaRepository.upsertOrderCreated(orderReadModel);
    }

    @Override
    public void updateStatus(Long orderId, String status) {
        orderReadModelJpaRepository.updateStatus(orderId, status);
    }

}
