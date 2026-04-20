package com.example.orderserver.order.application.usecase;

import com.example.orderserver.order.framework.web.request.OrderInputDTO;
import com.example.orderserver.order.framework.web.response.OrderOutputDTO;

public interface AddOrderUseCase {
    OrderOutputDTO create(Long memberId, Long storeId, OrderInputDTO request);
}
