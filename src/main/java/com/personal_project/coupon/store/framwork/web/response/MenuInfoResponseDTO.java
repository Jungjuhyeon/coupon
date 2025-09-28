package com.personal_project.coupon.store.framwork.web.response;

import com.personal_project.coupon.store.domain.model.Menu;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MenuInfoResponseDTO {
    private Long menuId;
    private String menuName;
    private Integer price;

    public static MenuInfoResponseDTO mapToDTO(Menu menu){
        return MenuInfoResponseDTO.builder()
                .menuId(menu.getId())
                .menuName(menu.getName())
                .price(menu.getPrice())
                .build();
    }
}
