package com.example.orderserver.order.infra.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuInfoFeignDTO {
    private Long menuId;
    private Integer price;
}
