package com.personal_project.coupon.coupon.domain;

import com.personal_project.coupon.coupon.domain.enumeration.CouponIssueStatus;
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

    public static CouponIssue create(Member member,Coupon coupon, LocalDateTime now){
        return CouponIssue.builder()
                .member(member)
                .coupon(coupon)
                .issuedAt(now)
                .couponIssueStatus(CouponIssueStatus.ISSUED)
                .build();
    }

}
