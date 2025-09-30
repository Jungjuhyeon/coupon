package com.personal_project.coupon.store.framwork.web.response;


import com.personal_project.coupon.store.domain.model.Menu;
import com.personal_project.coupon.store.domain.model.MenuCategory;
import com.personal_project.coupon.store.domain.model.Store;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Builder
public class StoreInfoOutputDTO {

    private String storeName;
    private String storePhoneNumber;
    private String address;

    private Long brandId;
    private String brandName;

    private Long storeCategoryId;
    private String storeCategoryName;

    private Long ownerId;
    private String ownerEmail;
    private String ownerPhoneNumber;
    private String ownerName;

    private List<MenuCategoryResponseDTO> menuCategoryList;

    public static StoreInfoOutputDTO mapToDTO(Store store){
        Map<MenuCategory, List<Menu>> groupedMenus = store.getMenuList().stream()
                .collect(Collectors.groupingBy(Menu::getMenuCategory));

        return StoreInfoOutputDTO.builder()
                .storeName(store.getName())
                .storePhoneNumber(store.getStorePhoneNumber())
                .address(store.getAddress())
                .brandId(store.getBrand().getId())
                .brandName(store.getBrand().getName())
                .storeCategoryId(store.getStoreCategory().getId())
                .storeCategoryName(store.getStoreCategory().getName())
                .ownerId(store.getOwner().getId())
                .ownerEmail(store.getOwner().getEmail())
                .ownerPhoneNumber(store.getOwner().getPhone())
                .ownerName(store.getOwner().getName())
                .menuCategoryList(MenuCategoryResponseDTO.mapToDTO(groupedMenus))
                .build();
    }
}
