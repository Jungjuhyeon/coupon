package com.personal_project.coupon.store.framwork.web.response;

import com.personal_project.coupon.store.domain.model.Menu;
import com.personal_project.coupon.store.domain.model.MenuCategory;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
public class MenuCategoryResponseDTO {
    private Long menuCategoryId;
    private String menuCategoryName;
    private List<MenuInfoResponseDTO> menus;

    public static List<MenuCategoryResponseDTO> mapToDTO(Map<MenuCategory, List<Menu>> groupedMenus){
        return groupedMenus.entrySet().stream()
                .sorted((e1, e2) -> e1.getKey().getId().compareTo(e2.getKey().getId()))

                .map(entry ->
                         MenuCategoryResponseDTO.builder()
                        .menuCategoryId(entry.getKey().getId())
                        .menuCategoryName(entry.getKey().getName())
                        .menus(entry.getValue().stream()
                                .map(MenuInfoResponseDTO::mapToDTO)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}