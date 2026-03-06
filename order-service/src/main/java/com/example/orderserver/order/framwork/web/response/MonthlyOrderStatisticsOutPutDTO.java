package com.example.orderserver.order.framwork.web.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonthlyOrderStatisticsOutPutDTO {
    private Integer month;
    private Long totalOrderCnt; //주문개수
    private Long totalOrderPrice; //주문 총합
    private Long totalMenuCnt; //메뉴 개수
    private Long totalMenuQuantity ; //메뉴총합

    public MonthlyOrderStatisticsOutPutDTO mapToDTO(Integer month,Long totalOrderCnt,
                                                    Long totalOrderPrice,Long totalMenuCnt,
                                                    Long totalMenuQuantity) {
        return MonthlyOrderStatisticsOutPutDTO.builder()
                .month(month)
                .totalMenuCnt(totalOrderCnt)
                .totalOrderPrice(totalOrderPrice)
                .totalMenuCnt(totalMenuCnt)
                .totalMenuQuantity(totalMenuQuantity)
                .build();
    }
}
