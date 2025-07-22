package com.personal_project.coupon.coupon.domain.model;

import com.personal_project.coupon.coupon.domain.model.enumeration.EventStatus;
import com.personal_project.coupon.coupon.framwork.web.request.EventInfoDTO;
import com.personal_project.coupon.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.personal_project.coupon.coupon.domain.model.enumeration.EventStatus.START;

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

    private LocalDateTime startDateTime; //이벤트 시작 시간

    private LocalDateTime endDateTime; //이벤트 종료 시간

    private LocalTime dailyStartTime; //매일 쿠폰 발급 시작 시간

    private LocalTime dailyEndTime; //매일 쿠폰 발급 종료 시간

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus; //시작전, 시작, 마감

    public static Event create(EventInfoDTO eventInfoDTO){
        return Event.builder()
                .name(eventInfoDTO.getName())
                .startDateTime(eventInfoDTO.getStartDateTime())
                .endDateTime(eventInfoDTO.getEndDateTime())
                .dailyStartTime(eventInfoDTO.getDailyStartTime())
                .dailyEndTime(eventInfoDTO.getDailyEndTime())
                .eventStatus(START)
                .build();
    }

}
