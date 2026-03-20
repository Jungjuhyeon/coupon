package com.example.orderserver.order.infra.persistence;

import com.example.orderserver.order.domain.model.document.OrderReadModel;

public interface OrderReadModelCustomRepository {
    void upsertOrderCreated(OrderReadModel document);
    void updateStatus(Long orderId, String status);
}
