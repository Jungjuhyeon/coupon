package com.personal_project.coupon.coupon.application.service.sync;

import com.personal_project.coupon.coupon.application.outputport.CouponIssueOutputPort;
import com.personal_project.coupon.coupon.application.outputport.CouponOutputPort;
import com.personal_project.coupon.coupon.domain.model.Coupon;
import com.personal_project.coupon.coupon.domain.model.CouponIssue;
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
    private final CouponOutputPort couponOutputPort;
    private final CouponIssueOutputPort couponIssueOutputPort;

    @Transactional
    public void saveIssuedCoupon(Long couponId, Long memberId, LocalDateTime now) {
        Member member = memberOutputPort.findById(memberId)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.USER_NOT_FOUND));

        Coupon coupon = couponOutputPort.findById(couponId)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.COUPON_NOT_FOUND));

        coupon.increaseStock();

        CouponIssue couponIssue = CouponIssue.create(member, coupon, now);
        couponIssueOutputPort.save(couponIssue);

    }
}
