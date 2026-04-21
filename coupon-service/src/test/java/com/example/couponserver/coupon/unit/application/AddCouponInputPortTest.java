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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

        // CouponInfoDTO: private 필드만 있으므로 ReflectionTestUtils로 직접 설정
        CouponInfoDTO couponInfoDTO = new CouponInfoDTO();
        ReflectionTestUtils.setField(couponInfoDTO, "promotionId", 1L);
        ReflectionTestUtils.setField(couponInfoDTO, "discountType", DiscountType.FIXED_AMOUNT);
        ReflectionTestUtils.setField(couponInfoDTO, "discountValue", 1000);
        ReflectionTestUtils.setField(couponInfoDTO, "maxQuantity", 100);
        ReflectionTestUtils.setField(couponInfoDTO, "startDate", startDate);
        ReflectionTestUtils.setField(couponInfoDTO, "endDate", endDate);
        ReflectionTestUtils.setField(couponInfoDTO, "usageStartDateTime", usageStart);
        ReflectionTestUtils.setField(couponInfoDTO, "usageEndDateTime", usageEnd);

        // Promotion: builder로 실제 도메인 객체 생성
        Promotion promotion = Promotion.builder()
                .name("테스트 프로모션")
                .startDateTime(usageStart.minusHours(1))
                .endDateTime(usageEnd)
                .dailyStartTime(LocalTime.of(0, 0))
                .dailyEndTime(LocalTime.of(23, 59))
                .build();
        when(promotionOutputPort.findById(1L)).thenReturn(Optional.of(promotion));

        // Coupon: 실제 도메인 객체 생성 (factory method 사용)
        Coupon savedCoupon = Coupon.create(promotion, couponInfoDTO);
        when(couponOutputPort.save(any(Coupon.class))).thenReturn(savedCoupon);

        doNothing().when(couponCacheOutputPort).saveCouponData(any(), anyInt(), any(), any());

        // when
        CouponOutPutDTO result = addCouponInputPort.addCoupon(couponInfoDTO);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getDiscountType()).isEqualTo(DiscountType.FIXED_AMOUNT);
        assertThat(result.getDiscountValue()).isEqualTo(1000);
        assertThat(result.getMaxQuantity()).isEqualTo(100);
        assertThat(result.getStartDate()).isEqualTo(startDate);
        assertThat(result.getEndDate()).isEqualTo(endDate);
        verify(couponOutputPort).save(any(Coupon.class));
        verify(couponCacheOutputPort).saveCouponData(any(), anyInt(), any(), any());
    }

    @Test
    @DisplayName("존재하지 않는 프로모션으로 쿠폰 등록 실패")
    void addCoupon_fail_when_promotion_not_found() {
        // given
        CouponInfoDTO couponInfoDTO = new CouponInfoDTO();
        ReflectionTestUtils.setField(couponInfoDTO, "promotionId", 999L);
        when(promotionOutputPort.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> addCouponInputPort.addCoupon(couponInfoDTO))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(CouponErrorCode.PROMOTION_NOT_FOUND));
    }
}
