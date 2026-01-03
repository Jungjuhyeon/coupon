package com.example.orderserver.order.infra.store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreOrderViewFeignDTO {
    private String storeName;
    private String brandName;
    private String categoryName;
    List<MenuOrderViewFeignDTO> menuOrderViewFeignDTOList;
}
