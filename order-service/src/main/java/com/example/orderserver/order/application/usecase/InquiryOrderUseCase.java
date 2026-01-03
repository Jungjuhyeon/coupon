package com.example.orderserver.order.application.usecase;

import com.example.orderserver.order.domain.model.document.OrderSummaryDocument;
import com.example.orderserver.order.framwork.web.response.OrderInfoOutPutDTO;

import java.util.List;

public interface InquiryOrderUseCase {

    public OrderInfoOutPutDTO getOrderDetail(Long memberId, Long orderId);

    public List<OrderSummaryDocument> getOrder(Long memberId);
}
