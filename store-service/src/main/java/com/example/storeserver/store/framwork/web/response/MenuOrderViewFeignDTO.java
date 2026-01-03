package com.example.storeserver.store.framwork.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MenuOrderViewFeignDTO {
    private Long menuId;
    private String name;

    public static MenuOrderViewFeignDTO mapToDTO(Long menuId, String name) {
        return MenuOrderViewFeignDTO.builder()
                .menuId(menuId)
                .name(name)
                .build();
    }
}
