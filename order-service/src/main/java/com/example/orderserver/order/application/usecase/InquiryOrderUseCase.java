package com.example.orderserver.order.application.usecase;

import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.domain.model.document.OrderReadModel;
import com.example.orderserver.order.framwork.web.response.OrderInfoOutPutDTO;

import java.util.List;

public interface InquiryOrderUseCase {

    OrderInfoOutPutDTO getOrderDetail(Long memberId, Long orderId);
    List<OrderReadModel> getOrder(Long memberId);
    Order getOrderById(Long orderId);

}
