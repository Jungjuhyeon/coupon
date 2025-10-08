package com.personal_project.coupon.order.application.usecase;


import com.personal_project.coupon.order.framwork.web.response.OrderInfoOutPutDTO;
import com.personal_project.coupon.order.framwork.web.response.OrderSummaryOutputDTO;

import java.util.List;

public interface InquiryOrderUseCase {

    public OrderInfoOutPutDTO getOrderDetail(Long memberId, Long orderId);

    public List<OrderSummaryOutputDTO> getOrder(Long memberId);
}
