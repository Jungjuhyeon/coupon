package com.example.orderserver.order.framwork.web.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonthlyOrderStatisticsOutPutDTO {
    private Integer month;
    private Long total_orders; //총 주문 개수
    private Long total_menu_count; //주문당 총 메뉴 개수
    private Long total_order_price; //총 주문 가격
    private Long total_menu_quantity ; //메뉴 총 개수

    public MonthlyOrderStatisticsOutPutDTO mapToDTO(Integer month,Long total_orders,
                                                    Long total_menu_count,Long total_order_price,
                                                    Long total_menu_quantity) {
        return MonthlyOrderStatisticsOutPutDTO.builder()
                .month(month)
                .total_orders(total_orders)
                .total_menu_count(total_menu_count)
                .total_order_price(total_order_price)
                .total_menu_quantity(total_menu_quantity)
                .build();
    }
}
