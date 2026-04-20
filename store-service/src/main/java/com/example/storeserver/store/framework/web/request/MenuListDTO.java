package com.example.storeserver.store.framework.web.request;

import lombok.Getter;

import java.util.List;

@Getter
public class MenuListDTO {
    private Long storeId;
    private List<MenuInfoDTO> list;
}
