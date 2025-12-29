package com.example.storeserver.store.framwork.web.response;


import com.example.storeserver.store.domain.model.Menu;
import com.example.storeserver.store.domain.model.MenuCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
public class MenuCategoryDTO {
    private Long menuCategoryId;
    private String menuCategoryName;
    private List<MenuInfoDTO> menus;

    public static List<MenuCategoryDTO> mapToDTO(Map<MenuCategory, List<Menu>> groupedMenus){
        return groupedMenus.entrySet().stream()
                .sorted((e1, e2) -> e1.getKey().getId().compareTo(e2.getKey().getId()))

                .map(entry ->
                         MenuCategoryDTO.builder()
                        .menuCategoryId(entry.getKey().getId())
                        .menuCategoryName(entry.getKey().getName())
                        .menus(entry.getValue().stream()
                                .map(MenuInfoDTO::mapToDTO)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}