package com.personal_project.coupon.store.framwork.web.response;

import com.personal_project.coupon.store.domain.model.Menu;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MenuOutputDTO {

    private Long storeId;
    private List<MenuInfoResponseDTO> menuList;

    public static MenuOutputDTO mapToDTO(Long storeId, List<Menu> menuList){
        List<MenuInfoResponseDTO> items = menuList.stream()
                .map(menu -> new MenuInfoResponseDTO(menu.getId(), menu.getName(), menu.getPrice()))
                .toList();
        return new MenuOutputDTO(storeId, items);
    }
}
