package com.personal_project.coupon.coupon.framwork.web.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.personal_project.coupon.coupon.domain.enumeration.DiscountType;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class CouponInfoDTO {
    private Long eventId;

    private DiscountType discountType;

    private Integer discountValue;

    private Integer maxQuantity;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate; //쿠폰 발행 시작 시간

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate; //쿠폰 발행 종료 시간

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime usageStartDateTime; //쿠폰 사용 가능 시작 시간

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime usageEndDateTime; //쿠폰 사용 가능 종료 시간

}
