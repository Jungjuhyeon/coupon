package com.personal_project.coupon.coupon.domain;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
public class EventCache {
    private final Long id;
    private final LocalTime dailyStartTime;
    private final LocalTime dailyEndTime;
    private final LocalDateTime startDateTime;
    private final LocalDateTime endDateTime;

    public static EventCache create(Long id,
                                    LocalTime dailyStartTime,LocalTime dailyEndTime,
                                    LocalDateTime startDateTime, LocalDateTime endDateTime){
        return EventCache.builder()
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
