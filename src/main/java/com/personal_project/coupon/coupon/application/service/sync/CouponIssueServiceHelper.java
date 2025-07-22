package com.personal_project.coupon.coupon.application.service.sync;

import com.personal_project.coupon.coupon.application.outputport.CouponIssueOutPort;
import com.personal_project.coupon.coupon.application.outputport.CouponOutPort;
import com.personal_project.coupon.coupon.domain.entity.Coupon;
import com.personal_project.coupon.coupon.domain.entity.CouponIssue;
import com.personal_project.coupon.global.exception.BusinessException;
import com.personal_project.coupon.global.exception.errorcode.CommonErrorCode;
import com.personal_project.coupon.member.applicaion.outputport.MemberOutputPort;
import com.personal_project.coupon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponIssueServiceHelper {
    private final MemberOutputPort memberOutputPort;
    private final CouponOutPort couponOutPort;
    private final CouponIssueOutPort couponIssueOutPort;

    @Transactional
    public void saveIssuedCoupon(Long couponId, Long memberId, LocalDateTime now) {
        Member member = memberOutputPort.findById(memberId)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.USER_NOT_FOUND));

        Coupon coupon = couponOutPort.findById(couponId)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.COUPON_NOT_FOUND));

        coupon.increaseStock();

        CouponIssue couponIssue = CouponIssue.create(member, coupon, now);
        couponIssueOutPort.save(couponIssue);
    }
}
