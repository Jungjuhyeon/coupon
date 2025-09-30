package com.personal_project.coupon.coupon.domain.model;

import com.personal_project.coupon.coupon.domain.model.enumeration.PromotionStatus;
import com.personal_project.coupon.coupon.framwork.web.request.PromotionIdInfoDTO;
import com.personal_project.coupon.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.personal_project.coupon.coupon.domain.model.enumeration.PromotionStatus.START;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Promotion extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "promotion_id")
    private Long id;

    private String name;

    private LocalDateTime startDateTime; //행사 시작 시간

    private LocalDateTime endDateTime; //행사 종료 시간

    private LocalTime dailyStartTime; //매일 쿠폰 발급 시작 시간

    private LocalTime dailyEndTime; //매일 쿠폰 발급 종료 시간

    @Enumerated(EnumType.STRING)
    private PromotionStatus promotionStatus; //시작전, 시작, 마감

    private Promotion(String name, LocalDateTime startDateTime, LocalDateTime endDateTime, LocalTime dailyStartTime, LocalTime dailyEndTime){
        this.name =name;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.dailyStartTime = dailyStartTime;
        this.dailyEndTime = dailyEndTime;
        this.promotionStatus = START;
    }
    public static Promotion create(PromotionIdInfoDTO promotionIdInfoDTO){
        return new Promotion(
                promotionIdInfoDTO.getName(),
                promotionIdInfoDTO.getStartDateTime(),
                promotionIdInfoDTO.getEndDateTime(),
                promotionIdInfoDTO.getDailyStartTime(),
                promotionIdInfoDTO.getDailyEndTime()
        );
    }

}
