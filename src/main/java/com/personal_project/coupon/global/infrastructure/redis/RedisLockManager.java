package com.personal_project.coupon.global.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisLockManager {

    private final RedisTemplate<String, String> redisTemplate;

    public Boolean lock(Object key) {
        int retryCount = 5;
        while (retryCount-- > 0) {
            if (redisTemplate.opsForValue().setIfAbsent(key.toString(), "lock", Duration.ofMillis(5000))) {
                return true;
            }
            try {
                Thread.sleep(100); // 100ms 대기 후 재시도
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return false; // 최대 시도 횟수를 넘었을 경우 false 반환
    }

    public Boolean unlock(Object key) {
        return redisTemplate.delete(key.toString());
    }

}
