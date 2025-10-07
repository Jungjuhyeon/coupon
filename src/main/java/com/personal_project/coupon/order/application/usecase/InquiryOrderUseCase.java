package com.personal_project.coupon.order.application.usecase;


import com.personal_project.coupon.order.framwork.web.response.OrderInfoOutPutDTO;

public interface InquiryOrderUseCase {

    public OrderInfoOutPutDTO getOrder(Long memberId, Long orderId);
}
