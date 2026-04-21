package com.example.storeserver.store.unit.application;

import com.example.common.global.exception.BusinessException;
import com.example.storeserver.store.application.inputport.AddStoreInputPort;
import com.example.storeserver.store.application.outputport.BrandOutputPort;
import com.example.storeserver.store.application.outputport.MemberOutputPort;
import com.example.storeserver.store.application.outputport.StoreCategoryOutputPort;
import com.example.storeserver.store.application.outputport.StoreOutputPort;
import com.example.storeserver.store.domain.model.Brand;
import com.example.storeserver.store.domain.model.Store;
import com.example.storeserver.store.domain.model.StoreCategory;
import com.example.storeserver.store.exception.StoreErrorCode;
import com.example.storeserver.store.framework.web.request.StoreInfoDTO;
import com.example.storeserver.store.framework.web.response.StoreIdOutputDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddStoreInputPortTest {

    @InjectMocks
    private AddStoreInputPort addStoreInputPort;

    @Mock
    private StoreCategoryOutputPort storeCategoryOutputPort;

    @Mock
    private BrandOutputPort brandOutputPort;

    @Mock
    private MemberOutputPort memberOutputPort;

    @Mock
    private StoreOutputPort storeOutputPort;

    @Test
    @DisplayName("가게 등록 성공")
    void create_success() {
        // given
        Long memberId = 1L;
        StoreInfoDTO request = mock(StoreInfoDTO.class);
        when(request.getStoreCategoryId()).thenReturn(1L);
        when(request.getBrandId()).thenReturn(1L);
        when(request.getName()).thenReturn("테스트 가게");
        when(request.getPhone()).thenReturn("02-1234-5678");
        when(request.getAddress()).thenReturn("서울시 강남구");

        StoreCategory mockCategory = mock(StoreCategory.class);
        Brand mockBrand = mock(Brand.class);
        Store mockStore = mock(Store.class);
        when(mockStore.getId()).thenReturn(1L);

        when(storeCategoryOutputPort.findById(1L)).thenReturn(Optional.of(mockCategory));
        when(brandOutputPort.findById(1L)).thenReturn(Optional.of(mockBrand));
        when(memberOutputPort.existsOwner(memberId)).thenReturn(true);
        when(storeOutputPort.save(any(Store.class))).thenReturn(mockStore);

        // when
        StoreIdOutputDTO result = addStoreInputPort.create(memberId, request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getStoreId()).isEqualTo(1L);
        verify(storeOutputPort).save(any(Store.class));
    }

    @Test
    @DisplayName("존재하지 않는 카테고리로 가게 등록 실패")
    void create_fail_when_category_not_found() {
        // given
        Long memberId = 1L;
        StoreInfoDTO request = mock(StoreInfoDTO.class);
        when(request.getStoreCategoryId()).thenReturn(999L);
        when(storeCategoryOutputPort.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> addStoreInputPort.create(memberId, request))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(StoreErrorCode.STORE_CATEGORY_NOT_FOUND));
    }

    @Test
    @DisplayName("존재하지 않는 브랜드로 가게 등록 실패")
    void create_fail_when_brand_not_found() {
        // given
        Long memberId = 1L;
        StoreInfoDTO request = mock(StoreInfoDTO.class);
        when(request.getStoreCategoryId()).thenReturn(1L);
        when(request.getBrandId()).thenReturn(999L);

        StoreCategory mockCategory = mock(StoreCategory.class);
        when(storeCategoryOutputPort.findById(1L)).thenReturn(Optional.of(mockCategory));
        when(brandOutputPort.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> addStoreInputPort.create(memberId, request))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(StoreErrorCode.BRAND_NOT_FOUND));
    }

    @Test
    @DisplayName("점주 자격 없는 회원으로 가게 등록 실패")
    void create_fail_when_owner_not_found() {
        // given
        Long memberId = 1L;
        StoreInfoDTO request = mock(StoreInfoDTO.class);
        when(request.getStoreCategoryId()).thenReturn(1L);
        when(request.getBrandId()).thenReturn(1L);

        StoreCategory mockCategory = mock(StoreCategory.class);
        Brand mockBrand = mock(Brand.class);
        when(storeCategoryOutputPort.findById(1L)).thenReturn(Optional.of(mockCategory));
        when(brandOutputPort.findById(1L)).thenReturn(Optional.of(mockBrand));
        when(memberOutputPort.existsOwner(memberId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> addStoreInputPort.create(memberId, request))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(StoreErrorCode.OWNER_NOT_FOUND));
    }
}
