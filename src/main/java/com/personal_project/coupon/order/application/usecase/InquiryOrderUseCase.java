package com.personal_project.coupon.order.application.usecase;


import com.personal_project.coupon.order.domain.model.document.OrderSummaryDocument;
import com.personal_project.coupon.order.framwork.web.response.OrderInfoOutPutDTO;

import java.util.List;

public interface InquiryOrderUseCase {

    public OrderInfoOutPutDTO getOrderDetail(Long memberId, Long orderId);

    public List<OrderSummaryDocument> getOrder(Long memberId);
}
