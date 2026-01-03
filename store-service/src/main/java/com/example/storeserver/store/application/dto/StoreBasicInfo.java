package com.example.storeserver.store.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StoreBasicInfo{
    Long storeId;
    String storeName;
    String brandName;
    String categoryName;
}