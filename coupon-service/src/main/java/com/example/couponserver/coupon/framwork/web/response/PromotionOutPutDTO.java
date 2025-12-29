package com.example.couponserver.coupon.framwork.web.response;

import com.example.couponserver.coupon.domain.model.Promotion;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Builder
public class PromotionOutPutDTO {
    private Long id;

    private String name;

    private LocalDateTime startDateTime; //이벤트 시작 시간

    private LocalDateTime endDateTime; //이벤트 종료 시간

    private LocalTime dailyStartTime; //매일 쿠폰 발급 시작 시간

    private LocalTime dailyEndTime; //매일 쿠폰 발급 종료 시간

    public static PromotionOutPutDTO mapToDTO(Promotion promotion){
        return PromotionOutPutDTO.builder()
                .id(promotion.getId())
                .name(promotion.getName())
                .startDateTime(promotion.getStartDateTime())
                .endDateTime(promotion.getEndDateTime())
                .dailyStartTime(promotion.getDailyStartTime())
                .dailyEndTime(promotion.getDailyEndTime())
                .build();
    }

}
