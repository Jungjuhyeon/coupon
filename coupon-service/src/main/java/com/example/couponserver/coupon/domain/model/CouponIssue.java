package com.example.couponserver.coupon.domain.model;

import com.example.common.global.entity.BaseEntity;
import com.example.couponserver.coupon.domain.model.enumeration.CouponIssueStatus;
import com.example.couponserver.coupon.domain.model.event.CouponIssuedEvent;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponIssue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_issue_id")
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private LocalDateTime issuedAt; //발급시간

    @Enumerated(EnumType.STRING)
    private CouponIssueStatus couponIssueStatus;     //발급됨, 사용됨

    private CouponIssue(Long memberId,Coupon coupon, LocalDateTime now){
        this.memberId = memberId;
        this.coupon = coupon;
        this.issuedAt = now;
        this.couponIssueStatus = CouponIssueStatus.ISSUED;
    }
    //이벤트 생성
    public static CouponIssuedEvent createCouponIssueEvent(Long couponId, Long memberId, LocalDateTime curTime){
        return new CouponIssuedEvent(couponId,memberId,curTime);
    }

    public static CouponIssue create(Long memberId,Coupon coupon, LocalDateTime now){
        return new CouponIssue(memberId, coupon, now);
    }

    public boolean isUsable() {
        return this.couponIssueStatus == CouponIssueStatus.ISSUED;
    }

}
