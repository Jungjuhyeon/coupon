package com.personal_project.coupon.order.framwork.web.request;

import com.personal_project.coupon.order.domain.model.enumeration.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter

public class OrderInputDTO {
    private Long couponIssueId;

    private List<OrderMenuInfoDTO> orderMenuInfoDTOList;

    private String deliveryAddress;

    private String comment;

}
