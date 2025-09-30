package com.personal_project.coupon.coupon.domain.model;

import com.personal_project.coupon.coupon.domain.model.enumeration.CouponIssueStatus;
import com.personal_project.coupon.coupon.domain.model.event.CouponIssuedEvent;
import com.personal_project.coupon.global.entity.BaseEntity;
import com.personal_project.coupon.member.domain.Member;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    private LocalDateTime issuedAt; //발급시간

    @Enumerated(EnumType.STRING)
    private CouponIssueStatus couponIssueStatus;     //발급됨, 사용됨

    private CouponIssue(Member member,Coupon coupon, LocalDateTime now){
        this.member = member;
        this.coupon = coupon;
        this.issuedAt = now;
        this.couponIssueStatus = CouponIssueStatus.ISSUED;
    }
    //이벤트 생성
    public static CouponIssuedEvent createCouponIssueEvent(Long couponId, Long memberId, LocalDateTime curTime){
        return new CouponIssuedEvent(couponId,memberId,curTime);
    }

    public static CouponIssue create(Member member,Coupon coupon, LocalDateTime now){
        return new CouponIssue(member, coupon, now);
    }

    public void couponUse() {
        this.couponIssueStatus = CouponIssueStatus.USED;
    }

}
