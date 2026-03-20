package com.example.orderserver.order.application.outputport;


import com.example.orderserver.order.domain.model.document.OrderReadModel;

import java.util.List;
import java.util.Optional;

public interface OrderReadModelOutputPort {

    List<OrderReadModel> findByMemberId(Long memberId);
    Optional<OrderReadModel> findByOrderId(Long orderId);

    void upsertOrderCreated(OrderReadModel orderReadModel); // save 대신 사용
    void updateStatus(Long orderId, String status);         // 상태 변경 전용
}

