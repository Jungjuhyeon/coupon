package com.example.couponserver.coupon.integration;

import com.example.common.global.exception.BusinessException;
import com.example.couponserver.coupon.application.inputport.IssueCouponInputPort;
import com.example.couponserver.coupon.application.outputport.CouponCacheOutputPort;
import com.example.couponserver.coupon.application.outputport.EventOutputPort;
import com.example.couponserver.coupon.application.service.CouponIssueEventPublisher;
import com.example.couponserver.coupon.application.service.CouponIssuer;
import com.example.couponserver.coupon.application.service.CouponValidator;
import com.example.couponserver.coupon.application.service.PromotionValidator;
import com.example.couponserver.coupon.application.usecase.IssueCouponUseCase;
import com.example.couponserver.coupon.exception.CouponErrorCode;
import com.example.couponserver.coupon.infra.redisadapter.CouponRedisAdapter;
import com.example.couponserver.coupon.infra.redisadapter.PromotionIdRedisAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * IssueCoupon 전체 흐름 통합 테스트
 *
 * 검증 범위: IssueCouponInputPort → PromotionValidator → CouponValidator → CouponIssuer → 실제 Redis
 * - Spring 컨텍스트 없이 빈을 직접 조립 (Redisson 클러스터 설정 간섭 방지)
 * - 실제 Redis 컨테이너로 Lua 스크립트 원자적 처리 검증
 * - Kafka 발행(EventOutputPort)은 이 테스트 관심 밖 → Mock
 */
@Testcontainers
class IssueCouponIntegrationTest {

    @Container
    static GenericContainer<?> redis = new GenericContainer<>("redis:7.0")
            .withExposedPorts(6379);

    private IssueCouponUseCase issueCouponUseCase;
    private CouponCacheOutputPort couponCacheOutputPort;
    // PromotionIdInfoDTO 생성자 없음 → 직접 Redis Hash에 적재하기 위해 template 보관
    private RedisTemplate<String, Object> objectRedisTemplate;

    private static final Long PROMOTION_ID = 1L;
    private static final Long COUPON_ID    = 100L;
    private static final Long MEMBER_ID    = 999L;

    @BeforeEach
    void setUp() {
        // 실제 Redis 컨테이너에 standalone 모드로 연결
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(
                redis.getHost(), redis.getMappedPort(6379)
        );
        connectionFactory.afterPropertiesSet();

        // CouponRedisAdapter → StringRedisTemplate
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(connectionFactory);
        stringRedisTemplate.afterPropertiesSet();

        // PromotionIdRedisAdapter → RedisTemplate<String, Object>
        objectRedisTemplate = new RedisTemplate<>();
        objectRedisTemplate.setConnectionFactory(connectionFactory);
        objectRedisTemplate.setKeySerializer(new StringRedisSerializer());
        objectRedisTemplate.setValueSerializer(new StringRedisSerializer());
        objectRedisTemplate.setHashKeySerializer(new StringRedisSerializer());
        objectRedisTemplate.setHashValueSerializer(new StringRedisSerializer());
        objectRedisTemplate.afterPropertiesSet();

        // 테스트 격리: 이전 테스트 데이터 제거
        stringRedisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();

        // 실제 Redis 어댑터
        CouponRedisAdapter couponRedisAdapter         = new CouponRedisAdapter(stringRedisTemplate);
        PromotionIdRedisAdapter promotionIdRedisAdapter = new PromotionIdRedisAdapter(objectRedisTemplate);

        // Kafka 발행은 테스트 관심 밖 → Mock (CompletableFuture null 이면 NPE)
        EventOutputPort eventOutputPort = Mockito.mock(EventOutputPort.class);
        when(eventOutputPort.occurCouponIssuedEvent(any()))
                .thenReturn(CompletableFuture.completedFuture(null));

        // 서비스 레이어 직접 조립
        CouponIssueEventPublisher publisher = new CouponIssueEventPublisher(eventOutputPort, couponRedisAdapter);
        PromotionValidator promotionValidator = new PromotionValidator(promotionIdRedisAdapter, publisher);
        CouponValidator couponValidator       = new CouponValidator(couponRedisAdapter, publisher);
        CouponIssuer couponIssuer             = new CouponIssuer(couponRedisAdapter, publisher);

        issueCouponUseCase    = new IssueCouponInputPort(promotionValidator, couponValidator, couponIssuer, publisher);
        couponCacheOutputPort = couponRedisAdapter;

        // 기본 테스트 데이터: 유효한 프로모션 + 재고 10개 쿠폰
        saveValidPromotion(PROMOTION_ID);
        couponCacheOutputPort.saveCouponData(
                COUPON_ID, 10,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(7)
        );
    }

    @Test
    @DisplayName("유효한 프로모션·쿠폰·재고 → 발급 성공 (예외 없음)")
    void issue_with_valid_conditions_succeeds() throws Exception {
        assertThatNoException()
                .isThrownBy(() -> issueCouponUseCase.issue(PROMOTION_ID, COUPON_ID, MEMBER_ID));
    }

    @Test
    @DisplayName("Redis에 없는 프로모션 → PROMOTION_NOT_FOUND 예외")
    void issue_with_nonexistent_promotion_throws_PROMOTION_NOT_FOUND() {
        assertThatThrownBy(() -> issueCouponUseCase.issue(999L, COUPON_ID, MEMBER_ID))
                .isInstanceOf(BusinessException.class)
                .satisfies(e -> {
                    assert ((BusinessException) e).getErrorCode() == CouponErrorCode.PROMOTION_NOT_FOUND;
                });
    }

    @Test
    @DisplayName("Redis에 없는 쿠폰 → COUPON_NOT_FOUND 예외")
    void issue_with_nonexistent_coupon_throws_COUPON_NOT_FOUND() {
        assertThatThrownBy(() -> issueCouponUseCase.issue(PROMOTION_ID, 999L, MEMBER_ID))
                .isInstanceOf(BusinessException.class)
                .satisfies(e -> {
                    assert ((BusinessException) e).getErrorCode() == CouponErrorCode.COUPON_NOT_FOUND;
                });
    }

    @Test
    @DisplayName("재고 1개 → 두 번째 회원 발급 시 COUPON_OUT_OF_STOCK 예외 (Lua 스크립트 원자성 검증)")
    void issue_when_stock_exhausted_throws_COUPON_OUT_OF_STOCK() throws Exception {
        Long stockOneCouponId = 200L;
        couponCacheOutputPort.saveCouponData(
                stockOneCouponId, 1,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(7)
        );
        // 첫 번째 회원이 마지막 재고 소진
        issueCouponUseCase.issue(PROMOTION_ID, stockOneCouponId, 1L);

        // when & then: 두 번째 회원 → 재고 없음
        assertThatThrownBy(() -> issueCouponUseCase.issue(PROMOTION_ID, stockOneCouponId, 2L))
                .isInstanceOf(BusinessException.class)
                .satisfies(e -> {
                    assert ((BusinessException) e).getErrorCode() == CouponErrorCode.COUPON_OUT_OF_STOCK;
                });
    }

    @Test
    @DisplayName("동일 회원 중복 발급 → COUPON_ALREADY_ISSUED 예외 (Lua 스크립트 중복 방지 검증)")
    void issue_duplicate_for_same_member_throws_COUPON_ALREADY_ISSUED() throws Exception {
        // 첫 번째 발급 성공
        issueCouponUseCase.issue(PROMOTION_ID, COUPON_ID, MEMBER_ID);

        // when & then: 동일 회원 재발급
        assertThatThrownBy(() -> issueCouponUseCase.issue(PROMOTION_ID, COUPON_ID, MEMBER_ID))
                .isInstanceOf(BusinessException.class)
                .satisfies(e -> {
                    assert ((BusinessException) e).getErrorCode() == CouponErrorCode.COUPON_ALREADY_ISSUED;
                });
    }

    /**
     * PromotionIdInfoDTO 는 생성자가 없어 PromotionCacheOutputPort.savePromotionTime() 호출 불가.
     * PromotionIdRedisAdapter 내부 로직과 동일하게 Redis Hash에 직접 적재.
     */
    private void saveValidPromotion(Long promotionId) {
        String key = "promotion:" + promotionId;
        LocalDateTime now = LocalDateTime.now();

        objectRedisTemplate.opsForHash().put(key, "dailyStartTime",
                LocalTime.of(0, 0, 0).format(DateTimeFormatter.ISO_LOCAL_TIME));
        objectRedisTemplate.opsForHash().put(key, "dailyEndTime",
                LocalTime.of(23, 59, 59).format(DateTimeFormatter.ISO_LOCAL_TIME));
        objectRedisTemplate.opsForHash().put(key, "startDateTime",
                now.minusHours(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        objectRedisTemplate.opsForHash().put(key, "endDateTime",
                now.plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
