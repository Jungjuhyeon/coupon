package com.personal_project.coupon.coupon.domain.model;


import com.personal_project.coupon.coupon.domain.model.enumeration.CouponStatus;
import com.personal_project.coupon.coupon.domain.model.enumeration.DiscountType;
import com.personal_project.coupon.coupon.framwork.web.request.CouponInfoDTO;
import com.personal_project.coupon.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private Integer discountValue;

    private Integer maxQuantity;

    private Integer issuedQuantity;

    private LocalDate startDate; //쿠폰 발핼일 시작일

    private LocalDate endDate; //쿠폰 발행 종료일

    private LocalDateTime usageStartDateTime; //쿠폰 사용 가능 시작 시간

    private LocalDateTime usageEndDateTime; //쿠폰 사용 가능 종료 시간

    @Enumerated(EnumType.STRING)
    private CouponStatus Couponstatus; //발급전 , 발급됨

    public static Coupon create(Event event,CouponInfoDTO couponInfoDTO){
        return Coupon.builder()
                .event(event)
                .discountType(couponInfoDTO.getDiscountType())
                .discountValue(couponInfoDTO.getDiscountValue())
                .maxQuantity(couponInfoDTO.getMaxQuantity())
                .issuedQuantity(0)
                .startDate(couponInfoDTO.getStartDate())
                .endDate(couponInfoDTO.getEndDate())
                .usageStartDateTime(couponInfoDTO.getUsageStartDateTime())
                .usageEndDateTime(couponInfoDTO.getUsageEndDateTime())
                .build();
    }

    public boolean isIssuable(LocalDateTime nowTime) {
        return (nowTime.isAfter(event.getStartDateTime()) && nowTime.isBefore(event.getEndDateTime()));
    }

    public boolean isQuantity() {
        return issuedQuantity < maxQuantity;
    }

    public void increaseStock() {
        ++issuedQuantity;
    }
}
