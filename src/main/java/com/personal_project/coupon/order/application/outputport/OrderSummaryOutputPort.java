package com.personal_project.coupon.order.application.outputport;

import com.personal_project.coupon.order.domain.model.document.OrderSummaryDocument;

import java.util.List;

public interface OrderSummaryOutputPort {

    public OrderSummaryDocument save(OrderSummaryDocument orderSummaryDocument);

    List<OrderSummaryDocument> findByMemberId(Long memberId);

}

