package com.example.storeserver.store.framwork.web.response;

import com.example.storeserver.store.domain.model.Menu;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuInfoDTO {
    private Long menuId;
    private String menuName;
    private Integer price;

    public static MenuInfoDTO mapToDTO(Menu menu){
        return MenuInfoDTO.builder()
                .menuId(menu.getId())
                .menuName(menu.getName())
                .price(menu.getPrice())
                .build();
    }
}
