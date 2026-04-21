package com.example.storeserver.store.unit.application;

import com.example.common.global.exception.BusinessException;
import com.example.storeserver.store.application.inputport.AddMenuInputPort;
import com.example.storeserver.store.application.outputport.MenuCategoryOutputPort;
import com.example.storeserver.store.application.outputport.MenuOutputPort;
import com.example.storeserver.store.application.outputport.StoreOutputPort;
import com.example.storeserver.store.domain.model.MenuCategory;
import com.example.storeserver.store.domain.model.Store;
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
        Store mockStore = mock(Store.class);
        when(mockStore.getId()).thenReturn(1L);

        MenuCategory mockCategory = mock(MenuCategory.class);

        MenuInfoDTO menuInfoDto = mock(MenuInfoDTO.class);
        when(menuInfoDto.getMenuCategoryId()).thenReturn(1L);
        when(menuInfoDto.getName()).thenReturn("테스트 메뉴");
        when(menuInfoDto.getPrice()).thenReturn(10000);

        MenuListDTO request = mock(MenuListDTO.class);
        when(request.getStoreId()).thenReturn(1L);
        when(request.getList()).thenReturn(List.of(menuInfoDto));

        when(storeOutputPort.findById(1L)).thenReturn(Optional.of(mockStore));
        when(menuCategoryOutputPort.findById(1L)).thenReturn(Optional.of(mockCategory));
        when(menuOutputPort.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        MenuOutputDTO result = addMenuInputPort.create(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getStoreId()).isEqualTo(1L);
        verify(menuOutputPort).saveAll(any());
    }

    @Test
    @DisplayName("존재하지 않는 가게로 메뉴 등록 실패")
    void create_fail_when_store_not_found() {
        // given
        MenuListDTO request = mock(MenuListDTO.class);
        when(request.getStoreId()).thenReturn(999L);
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
        Store mockStore = mock(Store.class);

        MenuInfoDTO menuInfoDto = mock(MenuInfoDTO.class);
        when(menuInfoDto.getMenuCategoryId()).thenReturn(999L);

        MenuListDTO request = mock(MenuListDTO.class);
        when(request.getStoreId()).thenReturn(1L);
        when(request.getList()).thenReturn(List.of(menuInfoDto));

        when(storeOutputPort.findById(1L)).thenReturn(Optional.of(mockStore));
        when(menuCategoryOutputPort.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> addMenuInputPort.create(request))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(StoreErrorCode.MENU_CATEGORY_NOT_FOUND));
    }
}
