package com.example.orderserver.order.application.outputport;


import com.example.orderserver.order.domain.model.document.OrderSummaryDocument;

import java.util.List;

public interface OrderSummaryOutputPort {

    public OrderSummaryDocument save(OrderSummaryDocument orderSummaryDocument);

    List<OrderSummaryDocument> findByMemberId(Long memberId);

}

