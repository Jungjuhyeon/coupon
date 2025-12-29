package com.example.storeserver.store.framwork.web.response;



import com.example.storeserver.store.application.dto.OwnerInfo;
import com.example.storeserver.store.domain.model.Menu;
import com.example.storeserver.store.domain.model.MenuCategory;
import com.example.storeserver.store.domain.model.Store;
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

    private List<MenuCategoryDTO> menuCategoryList;

    public static StoreInfoOutputDTO mapToDTO(Store store, OwnerInfo ownerInfo){
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
                .ownerId(ownerInfo.getId())
                .ownerEmail(ownerInfo.getEmail())
                .ownerPhoneNumber(ownerInfo.getPhone())
                .ownerName(ownerInfo.getName())
                .menuCategoryList(MenuCategoryDTO.mapToDTO(groupedMenus))
                .build();
    }
}
