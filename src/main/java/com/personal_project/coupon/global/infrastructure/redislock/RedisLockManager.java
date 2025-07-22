package com.personal_project.coupon.global.infrastructure.redislock;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RedisLockManager {


    private final RedisTemplate<String, String> redisTemplate;
    private static final Duration LOCK_TTL = Duration.ofSeconds(10);

    public String lock(String key) {
        String uniqueId = UUID.randomUUID().toString();
        int retryCount = 5;

        while (retryCount-- > 0) {
            Boolean success = redisTemplate.opsForValue().setIfAbsent(key, uniqueId, LOCK_TTL);
            if (Boolean.TRUE.equals(success)) {
                return uniqueId;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return null; // 락 획득 실패
    }

    public void unlock(String key, String uniqueId) {
        String currentValue = redisTemplate.opsForValue().get(key);
        if (uniqueId.equals(currentValue)) {
            redisTemplate.delete(key);
        }
    }
}