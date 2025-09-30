package com.personal_project.coupon.coupon.domain.model;

import com.personal_project.coupon.coupon.domain.model.event.CouponIssuedLogEvent;
import com.personal_project.coupon.coupon.domain.model.event.EventType;
import com.personal_project.coupon.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponIssueLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_issue_log_id")
    private Long id;

    private Long memberId;

    private Long couponId;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private CouponIssueLog(Long memberId, Long couponId, EventType eventType){
        this.memberId = memberId;
        this.couponId = couponId;
        this.eventType = eventType;
    }

    public static CouponIssueLog create(Long memberId,Long couponId,EventType eventType){
        return new CouponIssueLog(memberId, couponId, eventType);
    }

    //이벤트 생성
    public static CouponIssuedLogEvent createCouponIssuedLogEvent(Long memberId, Long couponId, LocalDateTime curTime, EventType eventType){
        return new CouponIssuedLogEvent(couponId,memberId,curTime,eventType);
    }
}
