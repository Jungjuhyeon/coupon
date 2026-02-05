package com.example.orderserver.order.application.usecase;

import com.example.orderserver.order.domain.model.document.OrderReadModel;

public interface AddOrderReadModelUseCase {
    void addOrderReadModel(OrderReadModel document);
}
