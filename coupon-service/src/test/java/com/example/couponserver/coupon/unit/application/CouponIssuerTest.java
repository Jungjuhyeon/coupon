package com.example.couponserver.coupon.unit.application;

import com.example.common.global.exception.BusinessException;
import com.example.common.global.exception.errorcode.CommonErrorCode;
import com.example.couponserver.coupon.application.outputport.CouponCacheOutputPort;
import com.example.couponserver.coupon.application.service.CouponIssueEventPublisher;
import com.example.couponserver.coupon.application.service.CouponIssuer;
import com.example.couponserver.coupon.exception.CouponErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponIssuerTest {

    @InjectMocks
    private CouponIssuer couponIssuer;

    @Mock
    private CouponCacheOutputPort couponCacheOutputPort;

    @Mock
    private CouponIssueEventPublisher eventPublisher;

    @Test
    @DisplayName("쿠폰 발급 성공 - Lua 스크립트 result=1")
    void issue_success() {
        // given
        Long memberId = 1L;
        Long couponId = 1L;
        LocalDateTime now = LocalDateTime.now();
        LocalDate endDate = now.toLocalDate().plusDays(7);

        when(couponCacheOutputPort.checkStockAndIssueCoupon(memberId, couponId, now, endDate)).thenReturn(1L);

        // when & then (예외 없이 정상 종료)
        couponIssuer.issue(memberId, couponId, now, endDate);
    }

    @Test
    @DisplayName("재고 소진 시 COUPON_OUT_OF_STOCK 예외 발생 - result=0")
    void issue_fail_when_out_of_stock() {
        // given
        Long memberId = 1L;
        Long couponId = 1L;
        LocalDateTime now = LocalDateTime.now();
        LocalDate endDate = now.toLocalDate().plusDays(7);

        when(couponCacheOutputPort.checkStockAndIssueCoupon(memberId, couponId, now, endDate)).thenReturn(0L);
        doNothing().when(eventPublisher).publishFailLog(any(), any(), any(), any());

        // when & then
        assertThatThrownBy(() -> couponIssuer.issue(memberId, couponId, now, endDate))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(CouponErrorCode.COUPON_OUT_OF_STOCK));
    }

    @Test
    @DisplayName("중복 발급 시 COUPON_ALREADY_ISSUED 예외 발생 - result=2")
    void issue_fail_when_already_issued() {
        // given
        Long memberId = 1L;
        Long couponId = 1L;
        LocalDateTime now = LocalDateTime.now();
        LocalDate endDate = now.toLocalDate().plusDays(7);

        when(couponCacheOutputPort.checkStockAndIssueCoupon(memberId, couponId, now, endDate)).thenReturn(2L);
        doNothing().when(eventPublisher).publishFailLog(any(), any(), any(), any());

        // when & then
        assertThatThrownBy(() -> couponIssuer.issue(memberId, couponId, now, endDate))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(CouponErrorCode.COUPON_ALREADY_ISSUED));
    }

    @Test
    @DisplayName("Redis 예외 발생 시 REDIS_SCRIPT_ERROR 예외 발생")
    void issue_fail_when_redis_throws() {
        // given
        Long memberId = 1L;
        Long couponId = 1L;
        LocalDateTime now = LocalDateTime.now();
        LocalDate endDate = now.toLocalDate().plusDays(7);

        when(couponCacheOutputPort.checkStockAndIssueCoupon(memberId, couponId, now, endDate))
                .thenThrow(new RuntimeException("Redis connection error"));

        // when & then
        assertThatThrownBy(() -> couponIssuer.issue(memberId, couponId, now, endDate))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(CommonErrorCode.REDIS_SCRIPT_ERROR));
    }
}
