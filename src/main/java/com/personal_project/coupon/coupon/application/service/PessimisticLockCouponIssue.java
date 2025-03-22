package com.personal_project.coupon.coupon.application.service;

import com.personal_project.coupon.coupon.application.outputport.CouponIssueOutPort;
import com.personal_project.coupon.coupon.application.outputport.CouponOutPort;
import com.personal_project.coupon.coupon.application.outputport.EventOutport;
import com.personal_project.coupon.coupon.application.usercase.CouponIssueFacade;
import com.personal_project.coupon.coupon.domain.Coupon;
import com.personal_project.coupon.coupon.domain.Event;
import com.personal_project.coupon.global.exception.BusinessException;
import com.personal_project.coupon.global.exception.errorcode.CommonErrorCode;
import com.personal_project.coupon.member.applicaion.outputport.MemberOutputPort;
import com.personal_project.coupon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class PessimisticLockCouponIssue implements CouponIssueFacade {

    private final MemberOutputPort memberOutputPort;
    private final EventOutport eventOutport;
    private final CouponOutPort couponOutPort;
    private final CouponIssueOutPort couponIssueOutPort;

    @Override
    public void issueCoupon(Long eventId, Long couponId, Long memberId){
        //유저 조회
        Member member = memberOutputPort.findById(memberId).orElseThrow(()-> new BusinessException(CommonErrorCode.USER_EMAIL_NOT_FOUND));

        //이벤트 조회
        Event event = eventOutport.findById(eventId).orElseThrow(()-> new BusinessException(CommonErrorCode.EVENT_NOT_FOUND));

        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);

        // 이벤트 기간 및 발급 가능 시간 체크
        if(!event.isEventActive(now)){
            throw new BusinessException(CommonErrorCode.EVENT_NOT_ACTIVE);
        }

        //쿠폰 조회
        Coupon coupon = couponOutPort.findById(couponId).orElseThrow(()-> new BusinessException(CommonErrorCode.COUPON_NOT_FOUND));

        //해당 쿠폰 발급 가능 기간 체크
        if(!coupon.isIssuable(now)){
            throw new BusinessException(CommonErrorCode.COUPON_NOT_ACTIVE);
        }

        //해당 쿠폰 재고 부족
        if(!coupon.isQuantity()){
            throw new BusinessException(CommonErrorCode.COUPON_OUT_OF_STOCK);
        }

        //해당 유저가 쿠폰을 중복해서 가지는지
        if(couponIssueOutPort.existByUserIdAndCouponId(memberId, couponId)){
            throw new BusinessException(CommonErrorCode.COUPON_ALREADY_ISSUED);
        }

        com.personal_project.coupon.coupon.domain.CouponIssue couponIssue = com.personal_project.coupon.coupon.domain.CouponIssue.create(member,coupon,now);

        //히스토리 추가
        couponIssueOutPort.save(couponIssue);

        //재고 증가
        coupon.increaseStock();
    }

}
