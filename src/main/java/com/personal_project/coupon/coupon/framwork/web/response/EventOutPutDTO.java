package com.personal_project.coupon.coupon.framwork.web.response;

import com.personal_project.coupon.coupon.domain.entity.Event;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
public class EventOutPutDTO {
    private Long id;

    private String name;

    private LocalDateTime startDateTime; //이벤트 시작 시간

    private LocalDateTime endDateTime; //이벤트 종료 시간

    private LocalTime dailyStartTime; //매일 쿠폰 발급 시작 시간

    private LocalTime dailyEndTime; //매일 쿠폰 발급 종료 시간

    public static EventOutPutDTO mapToDTO(Event event){
        return EventOutPutDTO.builder()
                .id(event.getId())
                .name(event.getName())
                .startDateTime(event.getStartDateTime())
                .endDateTime(event.getEndDateTime())
                .dailyStartTime(event.getDailyStartTime())
                .dailyEndTime(event.getDailyEndTime())
                .build();
    }

}
