package com.personal_project.coupon.coupon.domain;

import com.personal_project.coupon.coupon.domain.enumeration.EventStatus;
import com.personal_project.coupon.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_id")
    private Long id;

    private String name;

    private LocalDateTime startDate; //이벤트 시작 시간

    private LocalDateTime endDate; //이벤트 종료 시간

    private LocalTime dailyStartDate; //매일 쿠폰 발급 시작 시간

    private LocalTime dailyEndDate; //매일 쿠폰 발급 종료 시간

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus; //시작전, 시작, 마감

    public boolean isEventActive(LocalDateTime nowTime){
        if (nowTime.isBefore(startDate) || nowTime.isAfter(endDate)) {
            return false; // 이벤트 기간이 아닐 경우 비활성화
        }
        // 하루 중 특정 시간대 검사
        LocalTime nowTimeOnly = nowTime.toLocalTime();
        return nowTimeOnly.isAfter(dailyStartDate) && nowTimeOnly.isBefore(dailyEndDate);

    }
}
