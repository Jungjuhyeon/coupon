package com.example.couponserver.coupon.unit.domain;

import com.example.couponserver.coupon.domain.model.Coupon;
import com.example.couponserver.coupon.domain.model.CouponIssue;
import com.example.couponserver.coupon.domain.model.Promotion;
import com.example.couponserver.coupon.domain.model.cache.CouponCache;
import com.example.couponserver.coupon.domain.model.enumeration.CouponIssueStatus;
import com.example.couponserver.coupon.domain.model.enumeration.CouponStatus;
import com.example.couponserver.coupon.domain.model.enumeration.DiscountType;
import com.example.couponserver.coupon.domain.model.enumeration.PromotionStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class CouponDomainTest {

    // ── Coupon ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Coupon.create() 시 상태가 ISSUED로 설정된다")
    void coupon_create_sets_ISSUED_status() {
        // given
        Coupon coupon = buildCoupon(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(7));

        // then
        assertThat(coupon.getCouponstatus()).isEqualTo(CouponStatus.ISSUED);
    }

    @Test
    @DisplayName("isUsableNow() - 사용 가능 시간 내이면 true")
    void coupon_isUsableNow_returns_true_within_range() {
        // given
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);
        Coupon coupon = buildCoupon(start, end);

        // when & then
        assertThat(coupon.isUsableNow(LocalDateTime.now())).isTrue();
    }

    @Test
    @DisplayName("isUsableNow() - 사용 시작 전이면 false")
    void coupon_isUsableNow_returns_false_before_start() {
        // given
        LocalDateTime start = LocalDateTime.now().plusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(2);
        Coupon coupon = buildCoupon(start, end);

        // when & then
        assertThat(coupon.isUsableNow(LocalDateTime.now())).isFalse();
    }

    @Test
    @DisplayName("isUsableNow() - 사용 기간 종료 후이면 false")
    void coupon_isUsableNow_returns_false_after_end() {
        // given
        LocalDateTime start = LocalDateTime.now().minusHours(2);
        LocalDateTime end = LocalDateTime.now().minusHours(1);
        Coupon coupon = buildCoupon(start, end);

        // when & then
        assertThat(coupon.isUsableNow(LocalDateTime.now())).isFalse();
    }

    // ── CouponCache ──────────────────────────────────────────────────────

    @Test
    @DisplayName("CouponCache.isValid() - 발급 기간 내이면 true")
    void couponCache_isValid_returns_true_within_range() {
        // given
        LocalDate start = LocalDate.now().minusDays(1);
        LocalDate end = LocalDate.now().plusDays(7);
        CouponCache cache = CouponCache.create(1L, 100, start, end);

        // when & then
        assertThat(cache.isValid(LocalDate.now())).isTrue();
    }

    @Test
    @DisplayName("CouponCache.isValid() - 발급 시작 전이면 false")
    void couponCache_isValid_returns_false_before_start() {
        // given
        LocalDate start = LocalDate.now().plusDays(1);
        LocalDate end = LocalDate.now().plusDays(7);
        CouponCache cache = CouponCache.create(1L, 100, start, end);

        // when & then
        assertThat(cache.isValid(LocalDate.now())).isFalse();
    }

    @Test
    @DisplayName("CouponCache.isValid() - 발급 종료일 이후이면 false")
    void couponCache_isValid_returns_false_after_end() {
        // given
        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now().minusDays(1);
        CouponCache cache = CouponCache.create(1L, 100, start, end);

        // when & then
        assertThat(cache.isValid(LocalDate.now())).isFalse();
    }

    // ── CouponIssue ──────────────────────────────────────────────────────

    @Test
    @DisplayName("CouponIssue.create() 시 상태가 ISSUED로 설정된다")
    void couponIssue_create_sets_ISSUED_status() {
        // given
        Coupon coupon = buildCoupon(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(7));
        CouponIssue issue = CouponIssue.create(1L, coupon, LocalDateTime.now());

        // then
        assertThat(issue.getCouponIssueStatus()).isEqualTo(CouponIssueStatus.ISSUED);
    }

    @Test
    @DisplayName("CouponIssue.isUsable() - ISSUED 상태이면 true")
    void couponIssue_isUsable_returns_true_when_ISSUED() {
        // given
        Coupon coupon = buildCoupon(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(7));
        CouponIssue issue = CouponIssue.create(1L, coupon, LocalDateTime.now());

        // when & then
        assertThat(issue.isUsable()).isTrue();
    }

    @Test
    @DisplayName("CouponIssue.isUsable() - USED 상태이면 false")
    void couponIssue_isUsable_returns_false_when_USED() {
        // given
        Coupon coupon = buildCoupon(LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(7));
        CouponIssue usedIssue = CouponIssue.builder()
                .memberId(1L)
                .coupon(coupon)
                .issuedAt(LocalDateTime.now())
                .couponIssueStatus(CouponIssueStatus.USED)
                .build();

        // when & then
        assertThat(usedIssue.isUsable()).isFalse();
    }

    // ── helper ──────────────────────────────────────────────────────────

    private Coupon buildCoupon(LocalDateTime usageStart, LocalDateTime usageEnd) {
        Promotion promotion = Promotion.builder()
                .name("테스트 프로모션")
                .startDateTime(LocalDateTime.now().minusDays(1))
                .endDateTime(LocalDateTime.now().plusDays(7))
                .dailyStartTime(LocalTime.of(10, 0))
                .dailyEndTime(LocalTime.of(22, 0))
                .promotionStatus(PromotionStatus.START)
                .build();

        return Coupon.builder()
                .promotion(promotion)
                .discountType(DiscountType.FIXED_AMOUNT)
                .discountValue(1000)
                .maxQuantity(100)
                .startDate(LocalDate.now().minusDays(1))
                .endDate(LocalDate.now().plusDays(7))
                .usageStartDateTime(usageStart)
                .usageEndDateTime(usageEnd)
                .couponstatus(CouponStatus.ISSUED)
                .build();
    }
}
