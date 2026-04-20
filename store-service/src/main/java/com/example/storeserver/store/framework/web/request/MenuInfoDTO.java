package com.example.storeserver.store.framework.web.request;

import lombok.Getter;

@Getter
public class MenuInfoDTO {
    private Long menuCategoryId;
    private String name;
    private Integer price;
}
