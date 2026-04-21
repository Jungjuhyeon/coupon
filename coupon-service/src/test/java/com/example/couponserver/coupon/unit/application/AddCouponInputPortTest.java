package com.example.couponserver.coupon.unit.application;

import com.example.common.global.exception.BusinessException;
import com.example.couponserver.coupon.application.inputport.AddCouponInputPort;
import com.example.couponserver.coupon.application.outputport.CouponCacheOutputPort;
import com.example.couponserver.coupon.application.outputport.CouponOutputPort;
import com.example.couponserver.coupon.application.outputport.PromotionOutputPort;
import com.example.couponserver.coupon.domain.model.Coupon;
import com.example.couponserver.coupon.domain.model.Promotion;
import com.example.couponserver.coupon.domain.model.enumeration.DiscountType;
import com.example.couponserver.coupon.exception.CouponErrorCode;
import com.example.couponserver.coupon.framework.web.request.CouponInfoDTO;
import com.example.couponserver.coupon.framework.web.response.CouponOutPutDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddCouponInputPortTest {

    @InjectMocks
    private AddCouponInputPort addCouponInputPort;

    @Mock
    private CouponOutputPort couponOutputPort;

    @Mock
    private PromotionOutputPort promotionOutputPort;

    @Mock
    private CouponCacheOutputPort couponCacheOutputPort;

    @Test
    @DisplayName("쿠폰 등록 성공")
    void addCoupon_success() {
        // given
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(7);
        LocalDateTime usageStart = LocalDateTime.now();
        LocalDateTime usageEnd = LocalDateTime.now().plusDays(7);

        CouponInfoDTO couponInfoDTO = mock(CouponInfoDTO.class);
        when(couponInfoDTO.getPromotionId()).thenReturn(1L);
        when(couponInfoDTO.getDiscountType()).thenReturn(DiscountType.FIXED_AMOUNT);
        when(couponInfoDTO.getDiscountValue()).thenReturn(1000);
        when(couponInfoDTO.getMaxQuantity()).thenReturn(100);
        when(couponInfoDTO.getStartDate()).thenReturn(startDate);
        when(couponInfoDTO.getEndDate()).thenReturn(endDate);
        when(couponInfoDTO.getUsageStartDateTime()).thenReturn(usageStart);
        when(couponInfoDTO.getUsageEndDateTime()).thenReturn(usageEnd);

        Promotion mockPromotion = mock(Promotion.class);
        when(promotionOutputPort.findById(1L)).thenReturn(Optional.of(mockPromotion));

        Coupon mockSavedCoupon = mock(Coupon.class);
        when(mockSavedCoupon.getPromotion()).thenReturn(mockPromotion);
        when(mockPromotion.getId()).thenReturn(1L);
        when(mockSavedCoupon.getDiscountType()).thenReturn(DiscountType.FIXED_AMOUNT);
        when(mockSavedCoupon.getDiscountValue()).thenReturn(1000);
        when(mockSavedCoupon.getMaxQuantity()).thenReturn(100);
        when(mockSavedCoupon.getStartDate()).thenReturn(startDate);
        when(mockSavedCoupon.getEndDate()).thenReturn(endDate);
        when(mockSavedCoupon.getUsageStartDateTime()).thenReturn(usageStart);
        when(mockSavedCoupon.getUsageEndDateTime()).thenReturn(usageEnd);
        when(couponOutputPort.save(any(Coupon.class))).thenReturn(mockSavedCoupon);

        doNothing().when(couponCacheOutputPort).saveCouponData(any(), anyInt(), any(), any());

        // when
        CouponOutPutDTO result = addCouponInputPort.addCoupon(couponInfoDTO);

        // then
        assertThat(result).isNotNull();
        verify(couponOutputPort).save(any(Coupon.class));
        verify(couponCacheOutputPort).saveCouponData(any(), anyInt(), any(), any());
    }

    @Test
    @DisplayName("존재하지 않는 프로모션으로 쿠폰 등록 실패")
    void addCoupon_fail_when_promotion_not_found() {
        // given
        CouponInfoDTO couponInfoDTO = mock(CouponInfoDTO.class);
        when(couponInfoDTO.getPromotionId()).thenReturn(999L);
        when(promotionOutputPort.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> addCouponInputPort.addCoupon(couponInfoDTO))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(CouponErrorCode.PROMOTION_NOT_FOUND));
    }
}
