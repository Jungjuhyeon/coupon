package com.example.couponserver.coupon.unit.application;

import com.example.common.global.exception.BusinessException;
import com.example.common.global.exception.errorcode.CommonErrorCode;
import com.example.couponserver.coupon.application.outputport.CouponCacheOutputPort;
import com.example.couponserver.coupon.application.service.CouponIssueEventPublisher;
import com.example.couponserver.coupon.application.service.CouponValidator;
import com.example.couponserver.coupon.domain.model.cache.CouponCache;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponValidatorTest {

    @InjectMocks
    private CouponValidator couponValidator;

    @Mock
    private CouponCacheOutputPort couponCacheOutputPort;

    @Mock
    private CouponIssueEventPublisher couponIssueEventPublisher;

    @Test
    @DisplayName("쿠폰 검증 성공 - endDate 반환")
    void validate_success() {
        // given
        Long memberId = 1L;
        Long couponId = 1L;
        LocalDateTime now = LocalDateTime.now();
        LocalDate startDate = now.toLocalDate().minusDays(1);
        LocalDate endDate = now.toLocalDate().plusDays(7);

        CouponCache couponCache = CouponCache.create(couponId, 100, startDate, endDate);
        when(couponCacheOutputPort.getCouponCache(couponId)).thenReturn(couponCache);

        // when
        LocalDate result = couponValidator.validate(memberId, couponId, now);

        // then
        assertThat(result).isEqualTo(endDate);
    }

    @Test
    @DisplayName("캐시에 쿠폰이 없으면 COUPON_NOT_FOUND 예외 발생")
    void validate_fail_when_coupon_cache_null() {
        // given
        Long memberId = 1L;
        Long couponId = 1L;
        LocalDateTime now = LocalDateTime.now();

        when(couponCacheOutputPort.getCouponCache(couponId)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> couponValidator.validate(memberId, couponId, now))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(CouponErrorCode.COUPON_NOT_FOUND));
    }

    @Test
    @DisplayName("쿠폰 발급 기간이 아니면 COUPON_NOT_ACTIVE 예외 발생")
    void validate_fail_when_coupon_not_active() {
        // given
        Long memberId = 1L;
        Long couponId = 1L;
        LocalDateTime now = LocalDateTime.now();
        LocalDate startDate = now.toLocalDate().plusDays(1);
        LocalDate endDate = now.toLocalDate().plusDays(7);

        CouponCache couponCache = CouponCache.create(couponId, 100, startDate, endDate);
        when(couponCacheOutputPort.getCouponCache(couponId)).thenReturn(couponCache);
        doNothing().when(couponIssueEventPublisher).publishFailLog(any(), any(), any(), any());

        // when & then
        assertThatThrownBy(() -> couponValidator.validate(memberId, couponId, now))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(CouponErrorCode.COUPON_NOT_ACTIVE));
    }

    @Test
    @DisplayName("Redis 에러 발생 시 REDIS_SCRIPT_ERROR 예외 발생")
    void validate_fail_when_redis_throws() {
        // given
        Long memberId = 1L;
        Long couponId = 1L;
        LocalDateTime now = LocalDateTime.now();

        when(couponCacheOutputPort.getCouponCache(couponId)).thenThrow(new RuntimeException("Redis error"));

        // when & then
        assertThatThrownBy(() -> couponValidator.validate(memberId, couponId, now))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(CommonErrorCode.REDIS_SCRIPT_ERROR));
    }
}
