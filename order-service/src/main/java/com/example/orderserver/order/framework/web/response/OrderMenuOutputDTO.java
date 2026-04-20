package com.example.orderserver.order.framework.web.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderMenuOutputDTO {
    private Long orderMenuId;
    private Long menuId;
    private String name;
    private Integer price;
    private Integer quantity;
    private Integer totalPrice;


    public static OrderMenuOutputDTO mapToDTO(Long orderMenuId, Long menuId,
                                              String name, Integer price,
                                              Integer quantity, Integer totalPrice){
        return OrderMenuOutputDTO.builder()
                .orderMenuId(orderMenuId)
                .menuId(menuId)
                .name(name)
                .price(price)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .build();
    }
}
