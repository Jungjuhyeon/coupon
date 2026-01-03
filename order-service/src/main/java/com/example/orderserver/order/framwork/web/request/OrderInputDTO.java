package com.example.orderserver.order.framwork.web.request;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderInputDTO {
    private Long couponIssueId;
    private List<OrderMenuInfoDTO> orderMenuInfoDTOList;
    private String deliveryAddress;
    private String comment;

}
