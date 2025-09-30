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
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    private Integer discountValue;

    private Integer maxQuantity;

    private LocalDate startDate; //쿠폰 발핼일 시작일

    private LocalDate endDate; //쿠폰 발행 종료일

    private LocalDateTime usageStartDateTime; //쿠폰 사용 가능 시작 시간

    private LocalDateTime usageEndDateTime; //쿠폰 사용 가능 종료 시간

    @Enumerated(EnumType.STRING)
    private CouponStatus Couponstatus; //발급전 , 발급됨

    private Coupon(Promotion promotion,DiscountType discountType,Integer discountValue, Integer maxQuantity,
                   LocalDate startDate, LocalDate endDate, LocalDateTime usageStartDateTime, LocalDateTime usageEndDateTime){
        this.promotion = promotion;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.maxQuantity =maxQuantity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageStartDateTime = usageStartDateTime;
        this.usageEndDateTime = usageEndDateTime;
        this.Couponstatus = CouponStatus.ISSUED;
    }
    public static Coupon create(Promotion promotion, CouponInfoDTO couponInfoDTO){
        return new Coupon(promotion,couponInfoDTO.getDiscountType(),couponInfoDTO.getDiscountValue(),
                couponInfoDTO.getMaxQuantity(),couponInfoDTO.getStartDate(),couponInfoDTO.getEndDate(),
                couponInfoDTO.getUsageStartDateTime(),couponInfoDTO.getUsageEndDateTime());
    }

    public boolean isUsableNow(LocalDateTime now) {
        return !now.isBefore(usageStartDateTime) && now.isBefore(usageEndDateTime);
    }

}
