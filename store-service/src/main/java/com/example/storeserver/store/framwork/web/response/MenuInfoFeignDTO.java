package com.example.storeserver.store.framwork.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MenuInfoFeignDTO {
    private Long menuId;
    private Integer price;

    public static MenuInfoFeignDTO mapToDTO(Long menuId, Integer price) {
        return MenuInfoFeignDTO.builder()
                .menuId(menuId)
                .price(price)
                .build();
    }
}
