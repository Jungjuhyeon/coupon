package com.personal_project.coupon.coupon.domain;


import com.personal_project.coupon.coupon.domain.enumeration.CouponStatus;
import com.personal_project.coupon.coupon.domain.enumeration.DiscountType;
import com.personal_project.coupon.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

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

    private LocalDateTime startTime; //쿠폰 발행 시작 시간

    private LocalDateTime endTime; //쿠폰 발행 종료 시간

    private LocalDateTime usageStartTime; //쿠폰 사용 가능 시작 시간

    private LocalDateTime usageEndTime; //쿠폰 사용 가능 종료 시간

    @Enumerated(EnumType.STRING)
    private CouponStatus Couponstatus; //발급전 , 발급됨

}
