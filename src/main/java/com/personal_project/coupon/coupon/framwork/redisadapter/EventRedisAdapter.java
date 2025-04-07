package com.personal_project.coupon.coupon.framwork.redisadapter;

import com.personal_project.coupon.coupon.application.outputport.EventCacheOutPort;
import com.personal_project.coupon.coupon.domain.EventCache;
import com.personal_project.coupon.coupon.framwork.web.request.EventInfoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
@Slf4j
@Repository
@RequiredArgsConstructor
public class EventRedisAdapter implements EventCacheOutPort {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String EVENT_KEY_PREFIX = "event:";
    private static final String DAILY_START_TIME= "dailyStartTime";
    private static final String DAILY_END_TIME = "dailyEndTime";
    private static final String START_DATE_TIME = "startDateTime";
    private static final String END_DATE_TIME = "endDateTime";

    private static final DateTimeFormatter FORMATTER_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    // 🔹 이벤트 시간 정보 저장 (Hash 구조 사용)
    public void saveEventTime(Long eventId, EventInfoDTO eventInfoDTO) {

        String key = EVENT_KEY_PREFIX + eventId;

        redisTemplate.opsForHash().put(key, DAILY_START_TIME, eventInfoDTO.getDailyStartTime().format(FORMATTER_TIME));
        redisTemplate.opsForHash().put(key, DAILY_END_TIME, eventInfoDTO.getDailyEndTime().format(FORMATTER_TIME));
        redisTemplate.opsForHash().put(key, START_DATE_TIME, eventInfoDTO.getStartDateTime().format(FORMATTER_DATE_TIME));
        redisTemplate.opsForHash().put(key, END_DATE_TIME, eventInfoDTO.getEndDateTime().format(FORMATTER_DATE_TIME));

        // 이벤트 종료 시간까지 TTL 설정
        long durationMs = Duration.between(LocalDateTime.now(), eventInfoDTO.getEndDateTime()).toMillis();
        redisTemplate.expire(key, durationMs, TimeUnit.MILLISECONDS);
    }

    @Override
    public EventCache getEventCache(Long eventId) {
        String key = EVENT_KEY_PREFIX + eventId;

        String dailyStartTimeStr = (String) redisTemplate.opsForHash().get(key, DAILY_START_TIME);
        String dailyEndTimeStr = (String) redisTemplate.opsForHash().get(key, DAILY_END_TIME);
        String startDateTimeStr = (String) redisTemplate.opsForHash().get(key, START_DATE_TIME);
        String endDateTimeStr = (String) redisTemplate.opsForHash().get(key, END_DATE_TIME);

        if (dailyStartTimeStr == null || dailyEndTimeStr == null ||startDateTimeStr == null||endDateTimeStr==null  ) {
            return null; // 이벤트가 존재하지 않거나 데이터가 없을 경우
        }

        LocalTime dailyStartTime = LocalTime.parse(dailyStartTimeStr);
        LocalTime dailyEndTime = LocalTime.parse(dailyEndTimeStr);
        LocalDateTime startDateTime = LocalDateTime.parse(startDateTimeStr);
        LocalDateTime endDateTime = LocalDateTime.parse(endDateTimeStr);

        return EventCache.create(eventId, dailyStartTime, dailyEndTime,startDateTime,endDateTime);
    }

    @Override
    public void deleteEventCache(Long eventId) {
        String key = EVENT_KEY_PREFIX + eventId;
        redisTemplate.delete(key);
    }
}
