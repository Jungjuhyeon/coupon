package com.personal_project.coupon.order.application.usecase;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal_project.coupon.order.framwork.web.request.OrderInputDTO;
import com.personal_project.coupon.order.framwork.web.response.OrderOutputDTO;

public interface AddOrderUseCase {

    public OrderOutputDTO create(Long memberId, Long storeId, OrderInputDTO request) throws JsonProcessingException;
}
