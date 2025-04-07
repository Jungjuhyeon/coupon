package com.personal_project.coupon.coupon.framwork.web.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class EventInfoDTO {
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDateTime; //이벤트 시작 시간

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDateTime; //이벤트 종료 시간

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime dailyStartTime; //매일 쿠폰 발급 시작 시간

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime dailyEndTime; //매일 쿠폰 발급 종료 시간
}
