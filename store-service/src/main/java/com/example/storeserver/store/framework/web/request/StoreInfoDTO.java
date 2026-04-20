package com.example.storeserver.store.framework.web.request;

import lombok.Getter;

@Getter
public class StoreInfoDTO {
    private Long storeCategoryId;
    private Long brandId;
    private String name;
    private String phone;
    private String address;
}
