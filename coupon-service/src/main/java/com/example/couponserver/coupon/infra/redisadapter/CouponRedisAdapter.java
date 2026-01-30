package com.example.couponserver.coupon.infra.redisadapter;

import com.example.couponserver.coupon.application.outputport.CouponCacheOutputPort;
import com.example.couponserver.coupon.domain.model.cache.CouponCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CouponRedisAdapter implements CouponCacheOutputPort {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String STOCK = "stock";
    private static final String START_DATE = "startDate";
    private static final String COUPON_DATE= "endDate";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
        public void saveCouponData(Long couponId, int stock, LocalDate startDate, LocalDate endDate) {
            String key = "coupon:{" + couponId + "}"; // ⭐ 핵심
            redisTemplate.opsForHash().put(key, STOCK, String.valueOf(stock));
            redisTemplate.opsForHash().put(key, START_DATE, startDate.format(FORMATTER));
            redisTemplate.opsForHash().put(key, COUPON_DATE, endDate.format(FORMATTER));

            // endDate의 00:00:00로 TTL 설정
            LocalDateTime expireTime = endDate.atStartOfDay().plusHours(3); // 2025-04-05 03:00:00
            long ttl = ChronoUnit.SECONDS.between(LocalDateTime.now(), expireTime);

            redisTemplate.expire(key, ttl, TimeUnit.SECONDS);

    }

    @Override
    public CouponCache getCouponCache(Long couponId) {
        String key = "coupon:{" + couponId + "}";

        String stockStr = (String) redisTemplate.opsForHash().get(key, STOCK);
        String startTimeStr = (String) redisTemplate.opsForHash().get(key, START_DATE);
        String endTimeStr = (String) redisTemplate.opsForHash().get(key, COUPON_DATE);

        if (stockStr == null || startTimeStr == null || endTimeStr == null) {
            return null; // 쿠폰 정보가 없으면 null 반환
        }

        int stock = Integer.parseInt(stockStr);
        LocalDate startTime = LocalDate.parse(startTimeStr, FORMATTER);
        LocalDate endTime = LocalDate.parse(endTimeStr, FORMATTER);

        return CouponCache.create(couponId, stock, startTime, endTime);
    }

    // 쿠폰 발급 처리 메소드
    public Object checkStockAndIssueCoupon(Long memberId, Long couponId) {
        // Lua 스크립트 정의
        String script =
                "local stock = tonumber(redis.call('hget', KEYS[1], 'stock')) " +
                        "if not stock or stock <= 0 then return 0 end " +
                        "if redis.call('exists', KEYS[2]) == 1 then return 2 end " +
                        "redis.call('hincrby', KEYS[1], 'stock', -1) " +
                        "redis.call('set', KEYS[2], 'issued') " +
                        "return 1";

//        List<String> keys = Arrays.asList(
//                COUPON_KEY_PREFIX + couponId,
//                "member:" + memberId + ":coupon:" + couponId
//        );  // 해시맵을 키로 설정
        List<String> keys = Arrays.asList(
                "coupon:{" + couponId + "}",
                "coupon:{" + couponId + "}:member:" + memberId
        );

        // Redis Lua 스크립트 실행
        return redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), keys);
    }

}
