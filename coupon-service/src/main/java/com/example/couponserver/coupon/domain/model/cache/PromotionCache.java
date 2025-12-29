package com.example.couponserver.coupon.domain.model.cache;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
public class PromotionCache {
    private final Long id;
    private final LocalTime dailyStartTime;
    private final LocalTime dailyEndTime;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public static PromotionCache create(Long id,
                                        LocalTime dailyStartTime, LocalTime dailyEndTime,
                                        LocalDateTime startDateTime, LocalDateTime endDateTime){
        return PromotionCache.builder()
                .id(id)
                .dailyStartTime(dailyStartTime)
                .dailyEndTime(dailyEndTime)
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .build();
    }
    public boolean isValid(LocalDateTime now) {
        LocalTime nowTime = now.toLocalTime();
        return !now.isBefore(startDateTime) && !now.isAfter(endDateTime) &&
                !nowTime.isBefore(dailyStartTime) && !nowTime.isAfter(dailyEndTime);
    }
}
