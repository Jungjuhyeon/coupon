package com.personal_project.coupon.coupon.framwork.web.response;

import com.personal_project.coupon.coupon.domain.model.Coupon;
import com.personal_project.coupon.coupon.domain.model.enumeration.DiscountType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class CouponOutPutDTO {
    private Long id;
    private Long eventId;
    private DiscountType discountType;
    private Integer discountValue;
    private Integer maxQuantity;
    private LocalDate startDate; //쿠폰 발행 시작 시간
    private LocalDate endDate; //쿠폰 발행 종료 시간
    private LocalDateTime usageStartDateTime; //쿠폰 사용 가능 시작 시간
    private LocalDateTime usageEndDateTime; //쿠폰 사용 가능 종료 시간

    public static CouponOutPutDTO mapToDTO(Coupon coupon){
        return CouponOutPutDTO.builder()
                .id(coupon.getId())
                .eventId(coupon.getEvent().getId())
                .discountType(coupon.getDiscountType())
                .discountValue(coupon.getDiscountValue())
                .maxQuantity(coupon.getMaxQuantity())
                .startDate(coupon.getStartDate())
                .endDate(coupon.getEndDate())
                .usageStartDateTime(coupon.getUsageStartDateTime())
                .usageEndDateTime(coupon.getUsageEndDateTime())
                .build();
    }
}
