package com.example.storeserver.store.framework.web.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StoreOrderViewFeignDTO {
    private String storeName;
    private String brandName;
    private String categoryName;
    List<MenuOrderViewFeignDTO> menuOrderViewFeignDTOList;

    public static StoreOrderViewFeignDTO mapToDTO(String storeName, String brandName, String categoryName, List<MenuOrderViewFeignDTO> menuOrderViewFeignDTOList){
        return StoreOrderViewFeignDTO.builder()
                .storeName(storeName)
                .brandName(brandName)
                .categoryName(categoryName)
                .menuOrderViewFeignDTOList(menuOrderViewFeignDTOList)
                .build();
    }
}
