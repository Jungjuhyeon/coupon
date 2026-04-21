package com.example.storeserver.store.unit.application;

import com.example.common.global.exception.BusinessException;
import com.example.storeserver.store.application.inputport.AddMenuInputPort;
import com.example.storeserver.store.application.outputport.MenuCategoryOutputPort;
import com.example.storeserver.store.application.outputport.MenuOutputPort;
import com.example.storeserver.store.application.outputport.StoreOutputPort;
import com.example.storeserver.store.domain.model.Brand;
import com.example.storeserver.store.domain.model.MenuCategory;
import com.example.storeserver.store.domain.model.Store;
import com.example.storeserver.store.domain.model.StoreCategory;
import com.example.storeserver.store.exception.StoreErrorCode;
import com.example.storeserver.store.framework.web.request.MenuInfoDTO;
import com.example.storeserver.store.framework.web.request.MenuListDTO;
import com.example.storeserver.store.framework.web.response.MenuOutputDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddMenuInputPortTest {

    @InjectMocks
    private AddMenuInputPort addMenuInputPort;

    @Mock
    private MenuOutputPort menuOutputPort;

    @Mock
    private StoreOutputPort storeOutputPort;

    @Mock
    private MenuCategoryOutputPort menuCategoryOutputPort;

    @Test
    @DisplayName("메뉴 등록 성공")
    void create_success() {
        // given
        // 도메인 객체 — builder/factory method로 실제 객체 생성 (의존 관계 순서대로 조립)
        StoreCategory storeCategory = StoreCategory.builder().name("한식").build();
        Brand brand = Brand.builder().name("테스트 브랜드").storeCategory(storeCategory).build();
        Store store = Store.create(brand, storeCategory, 1L, "테스트 가게", "02-1234-5678", "서울시 강남구");
        ReflectionTestUtils.setField(store, "id", 1L);

        MenuCategory menuCategory = MenuCategory.builder().name("메인메뉴").storeCategory(storeCategory).build();

        // MenuInfoDTO: private 필드만 있으므로 ReflectionTestUtils로 직접 설정
        MenuInfoDTO menuInfoDto = new MenuInfoDTO();
        ReflectionTestUtils.setField(menuInfoDto, "menuCategoryId", 1L);
        ReflectionTestUtils.setField(menuInfoDto, "name", "테스트 메뉴");
        ReflectionTestUtils.setField(menuInfoDto, "price", 10000);

        // MenuListDTO: private 필드만 있으므로 ReflectionTestUtils로 직접 설정
        MenuListDTO request = new MenuListDTO();
        ReflectionTestUtils.setField(request, "storeId", 1L);
        ReflectionTestUtils.setField(request, "list", List.of(menuInfoDto));

        when(storeOutputPort.findById(1L)).thenReturn(Optional.of(store));
        when(menuCategoryOutputPort.findById(1L)).thenReturn(Optional.of(menuCategory));
        when(menuOutputPort.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        MenuOutputDTO result = addMenuInputPort.create(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getStoreId()).isEqualTo(1L);
        assertThat(result.getMenuList()).hasSize(1);
        assertThat(result.getMenuList().get(0).getMenuName()).isEqualTo("테스트 메뉴");
        verify(menuOutputPort).saveAll(any());
    }

    @Test
    @DisplayName("존재하지 않는 가게로 메뉴 등록 실패")
    void create_fail_when_store_not_found() {
        // given
        MenuListDTO request = new MenuListDTO();
        ReflectionTestUtils.setField(request, "storeId", 999L);
        when(storeOutputPort.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> addMenuInputPort.create(request))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(StoreErrorCode.STORE_NOT_FOUND));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴 카테고리로 메뉴 등록 실패")
    void create_fail_when_menu_category_not_found() {
        // given
        StoreCategory storeCategory = StoreCategory.builder().name("한식").build();
        Brand brand = Brand.builder().name("테스트 브랜드").storeCategory(storeCategory).build();
        Store store = Store.create(brand, storeCategory, 1L, "테스트 가게", "02-1234-5678", "서울시 강남구");

        MenuInfoDTO menuInfoDto = new MenuInfoDTO();
        ReflectionTestUtils.setField(menuInfoDto, "menuCategoryId", 999L);

        MenuListDTO request = new MenuListDTO();
        ReflectionTestUtils.setField(request, "storeId", 1L);
        ReflectionTestUtils.setField(request, "list", List.of(menuInfoDto));

        when(storeOutputPort.findById(1L)).thenReturn(Optional.of(store));
        when(menuCategoryOutputPort.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> addMenuInputPort.create(request))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(StoreErrorCode.MENU_CATEGORY_NOT_FOUND));
    }
}
