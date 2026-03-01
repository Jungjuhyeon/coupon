package com.example.couponserver.coupon.infra.redisadapter;

import com.example.couponserver.coupon.application.outputport.CouponCacheOutputPort;
import com.example.couponserver.coupon.domain.model.cache.CouponCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
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
    public Object checkStockAndIssueCoupon(Long memberId, Long couponId, LocalDateTime now, LocalDate endDate) {
        LocalDateTime expireTime = endDate.atStartOfDay().plusHours(3);
        long ttlSeconds = ChronoUnit.SECONDS.between(LocalDateTime.now(), expireTime);
        // Lua 스크립트 정의
        String script =
                "local stock = tonumber(redis.call('hget', KEYS[1], 'stock')) " +
                        "if not stock or stock <= 0 then return 0 end " +
                        "if redis.call('sismember', KEYS[2], ARGV[1]) == 1 then return 2 end " +
                        "redis.call('hincrby', KEYS[1], 'stock', -1) " +
                        "redis.call('sadd', KEYS[2], ARGV[1]) " +
                        "redis.call('hset', KEYS[3], ARGV[1], ARGV[2]) " +
                        "redis.call('expire', KEYS[2], tonumber(ARGV[3])) " + // issued set TTL
                        "redis.call('expire', KEYS[3], tonumber(ARGV[3])) " + // ready_to_publish hash TTL
                        "return 1";

        List<String> keys = Arrays.asList(
                "coupon:{" + couponId + "}",          // stock hash
                "coupon:{" + couponId + "}:issued",   // 중복 체크 set
                "coupon:{" + couponId + "}:ready_to_publish"   // 재처리 set
        );

        return redisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                keys,
                String.valueOf(memberId),             // ARGV[1] : memberId
                String.valueOf(now),              // ARGV[2] : 발급시간
                String.valueOf(ttlSeconds)    // ARGV[3] : TTL
        );
    }

    public void remove(String key, String memberId){
        redisTemplate.opsForHash().delete(key, memberId);
    }

    public List<Map.Entry<Object, Object>> getReadyToPublishCoupons(Long couponId) {
        String key = "coupon:{" + couponId + "}:ready_to_publish";

        List<Map.Entry<Object, Object>> entries = new ArrayList<>();
        ScanOptions options = ScanOptions.scanOptions().count(300).build();

        try (Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(key, options)) {
            while (cursor.hasNext()) entries.add(cursor.next());
        } catch (Exception e) {
            log.error("[RetryScheduler] 쿠폰 조회 실패 couponId={}", couponId, e);
        }
        return entries;
    }

}
