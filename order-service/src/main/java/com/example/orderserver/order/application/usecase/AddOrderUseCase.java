package com.example.orderserver.order.application.usecase;

import com.example.orderserver.order.framwork.web.request.OrderInputDTO;
import com.example.orderserver.order.framwork.web.response.OrderOutputDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface AddOrderUseCase {

    public OrderOutputDTO create(Long memberId, Long storeId, OrderInputDTO request) throws JsonProcessingException;
}
