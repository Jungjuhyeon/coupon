package com.example.couponserver.coupon.integration;

import com.example.couponserver.coupon.domain.model.Coupon;
import com.example.couponserver.coupon.domain.model.Promotion;
import com.example.couponserver.coupon.domain.model.enumeration.CouponStatus;
import com.example.couponserver.coupon.domain.model.enumeration.DiscountType;
import com.example.couponserver.coupon.domain.model.enumeration.PromotionStatus;
import com.example.couponserver.coupon.infra.persistence.CouponJpaRepository;
import com.example.couponserver.coupon.infra.persistence.PromotionJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
class CouponRepositoryIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PromotionJpaRepository promotionJpaRepository;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Test
    @DisplayName("프로모션 저장 및 조회 성공")
    void savePromotion_and_findById() {
        // given
        Promotion promotion = Promotion.builder()
                .name("테스트 프로모션")
                .startDateTime(LocalDateTime.now().minusDays(1))
                .endDateTime(LocalDateTime.now().plusDays(7))
                .dailyStartTime(LocalTime.of(10, 0))
                .dailyEndTime(LocalTime.of(22, 0))
                .promotionStatus(PromotionStatus.START)
                .build();

        // when
        Promotion saved = promotionJpaRepository.save(promotion);
        entityManager.flush();
        entityManager.clear();
        Optional<Promotion> found = promotionJpaRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("테스트 프로모션");
        assertThat(found.get().getPromotionStatus()).isEqualTo(PromotionStatus.START);
    }

    @Test
    @DisplayName("쿠폰 저장 및 조회 성공")
    void saveCoupon_and_findById() {
        // given
        Promotion promotion = promotionJpaRepository.save(
                Promotion.builder()
                        .name("테스트 프로모션")
                        .startDateTime(LocalDateTime.now().minusDays(1))
                        .endDateTime(LocalDateTime.now().plusDays(7))
                        .dailyStartTime(LocalTime.of(10, 0))
                        .dailyEndTime(LocalTime.of(22, 0))
                        .promotionStatus(PromotionStatus.START)
                        .build()
        );

        Coupon coupon = Coupon.builder()
                .promotion(promotion)
                .discountType(DiscountType.FIXED_AMOUNT)
                .discountValue(1000)
                .maxQuantity(100)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(7))
                .usageStartDateTime(LocalDateTime.now())
                .usageEndDateTime(LocalDateTime.now().plusDays(7))
                .couponstatus(CouponStatus.ISSUED)
                .build();

        // when
        Coupon saved = couponJpaRepository.save(coupon);
        entityManager.flush();
        entityManager.clear();
        Optional<Coupon> found = couponJpaRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getDiscountType()).isEqualTo(DiscountType.FIXED_AMOUNT);
        assertThat(found.get().getMaxQuantity()).isEqualTo(100);
    }
}
