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
}
