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
import org.springframework.test.util.ReflectionTestUtils;

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

        // StoreInfoDTO: private 필드만 있으므로 ReflectionTestUtils로 직접 설정
        StoreInfoDTO request = new StoreInfoDTO();
        ReflectionTestUtils.setField(request, "storeCategoryId", 1L);
        ReflectionTestUtils.setField(request, "brandId", 1L);
        ReflectionTestUtils.setField(request, "name", "테스트 가게");
        ReflectionTestUtils.setField(request, "phone", "02-1234-5678");
        ReflectionTestUtils.setField(request, "address", "서울시 강남구");

        // 도메인 객체 — builder로 실제 객체 생성
        StoreCategory storeCategory = StoreCategory.builder().name("한식").build();
        Brand brand = Brand.builder().name("테스트 브랜드").storeCategory(storeCategory).build();

        // Store: factory method + ReflectionTestUtils로 id 설정
        Store savedStore = Store.create(brand, storeCategory, memberId, "테스트 가게", "02-1234-5678", "서울시 강남구");
        ReflectionTestUtils.setField(savedStore, "id", 1L);

        when(storeCategoryOutputPort.findById(1L)).thenReturn(Optional.of(storeCategory));
        when(brandOutputPort.findById(1L)).thenReturn(Optional.of(brand));
        when(memberOutputPort.existsOwner(memberId)).thenReturn(true);
        when(storeOutputPort.save(any(Store.class))).thenReturn(savedStore);

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
        StoreInfoDTO request = new StoreInfoDTO();
        ReflectionTestUtils.setField(request, "storeCategoryId", 999L);
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
        StoreInfoDTO request = new StoreInfoDTO();
        ReflectionTestUtils.setField(request, "storeCategoryId", 1L);
        ReflectionTestUtils.setField(request, "brandId", 999L);

        StoreCategory storeCategory = StoreCategory.builder().name("한식").build();
        when(storeCategoryOutputPort.findById(1L)).thenReturn(Optional.of(storeCategory));
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
        StoreInfoDTO request = new StoreInfoDTO();
        ReflectionTestUtils.setField(request, "storeCategoryId", 1L);
        ReflectionTestUtils.setField(request, "brandId", 1L);

        StoreCategory storeCategory = StoreCategory.builder().name("한식").build();
        Brand brand = Brand.builder().name("테스트 브랜드").storeCategory(storeCategory).build();
        when(storeCategoryOutputPort.findById(1L)).thenReturn(Optional.of(storeCategory));
        when(brandOutputPort.findById(1L)).thenReturn(Optional.of(brand));
        when(memberOutputPort.existsOwner(memberId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> addStoreInputPort.create(memberId, request))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(StoreErrorCode.OWNER_NOT_FOUND));
    }
}
