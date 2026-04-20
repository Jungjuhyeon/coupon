package com.example.storeserver.store.framework.web.response;

import com.example.storeserver.store.domain.model.Menu;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MenuOutputDTO {

    private Long storeId;
    private List<MenuInfoDTO> menuList;

    public static MenuOutputDTO mapToDTO(Long storeId, List<Menu> menuList){
        List<MenuInfoDTO> items = menuList.stream()
                .map(MenuInfoDTO::mapToDTO)
                .toList();
        return builder().storeId(storeId).menuList(items).build();
    }
}
