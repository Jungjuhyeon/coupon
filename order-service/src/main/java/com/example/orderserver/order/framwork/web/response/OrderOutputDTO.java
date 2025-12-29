package com.example.orderserver.order.framwork.web.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderOutputDTO {
    private Long orderId;

    public static OrderOutputDTO mapToDTO(Long orderId){
        return OrderOutputDTO.builder()
                .orderId(orderId)
                .build();
    }
}
