package com.example.storeserver.store.unit.domain;

import com.example.storeserver.store.domain.model.Brand;
import com.example.storeserver.store.domain.model.Menu;
import com.example.storeserver.store.domain.model.MenuCategory;
import com.example.storeserver.store.domain.model.Store;
import com.example.storeserver.store.domain.model.StoreCategory;
import com.example.storeserver.store.domain.model.enumeration.MenuStatus;
import com.example.storeserver.store.domain.model.enumeration.StoreStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StoreDomainTest {

    @Test
    @DisplayName("Store.create() 시 상태가 OPEN으로 자동 설정된다")
    void store_create_sets_OPEN_status() {
        // given
        StoreCategory storeCategory = StoreCategory.builder().name("한식").build();
        Brand brand = Brand.builder().storeCategory(storeCategory).name("테스트 브랜드").build();

        // when
        Store store = Store.create(brand, storeCategory, 1L, "테스트 가게", "02-1234-5678", "서울시 강남구");

        // then
        assertThat(store.getStatus()).isEqualTo(StoreStatus.OPEN);
    }

    @Test
    @DisplayName("Store.create() 시 전달한 필드값이 정확히 저장된다")
    void store_create_stores_all_fields_correctly() {
        // given
        StoreCategory storeCategory = StoreCategory.builder().name("한식").build();
        Brand brand = Brand.builder().storeCategory(storeCategory).name("테스트 브랜드").build();

        // when
        Store store = Store.create(brand, storeCategory, 1L, "테스트 가게", "02-1234-5678", "서울시 강남구");

        // then
        assertThat(store.getName()).isEqualTo("테스트 가게");
        assertThat(store.getStorePhoneNumber()).isEqualTo("02-1234-5678");
        assertThat(store.getAddress()).isEqualTo("서울시 강남구");
        assertThat(store.getOwnerId()).isEqualTo(1L);
        assertThat(store.getBrand()).isEqualTo(brand);
        assertThat(store.getStoreCategory()).isEqualTo(storeCategory);
    }

    @Test
    @DisplayName("Menu.create() 시 상태가 AVAILABLE로 자동 설정된다")
    void menu_create_sets_AVAILABLE_status() {
        // given
        StoreCategory storeCategory = StoreCategory.builder().name("한식").build();
        Brand brand = Brand.builder().storeCategory(storeCategory).name("테스트 브랜드").build();
        Store store = Store.create(brand, storeCategory, 1L, "테스트 가게", "02-1234-5678", "서울시 강남구");
        MenuCategory menuCategory = MenuCategory.builder().storeCategory(storeCategory).name("메인 메뉴").build();

        // when
        Menu menu = Menu.create(store, menuCategory, "불고기", 12000);

        // then
        assertThat(menu.getMenuStatus()).isEqualTo(MenuStatus.AVAILABLE);
    }

    @Test
    @DisplayName("Menu.create() 시 이름과 가격이 정확히 저장된다")
    void menu_create_stores_name_and_price() {
        // given
        StoreCategory storeCategory = StoreCategory.builder().name("한식").build();
        Brand brand = Brand.builder().storeCategory(storeCategory).name("테스트 브랜드").build();
        Store store = Store.create(brand, storeCategory, 1L, "테스트 가게", "02-1234-5678", "서울시 강남구");
        MenuCategory menuCategory = MenuCategory.builder().storeCategory(storeCategory).name("메인 메뉴").build();

        // when
        Menu menu = Menu.create(store, menuCategory, "불고기", 12000);

        // then
        assertThat(menu.getName()).isEqualTo("불고기");
        assertThat(menu.getPrice()).isEqualTo(12000);
    }
}
