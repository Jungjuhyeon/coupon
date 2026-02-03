package com.example.couponserver.coupon.application.inputport;

import com.example.couponserver.coupon.application.outputport.CouponIssueOutputPort;
import com.example.couponserver.coupon.application.usecase.AddCouponIssueUseCase;
import com.example.couponserver.coupon.domain.model.Coupon;
import com.example.couponserver.coupon.domain.model.CouponIssue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Transactional
public class AddCouponIssueInputPort implements AddCouponIssueUseCase {
    private final CouponIssueOutputPort couponIssueOutputPort;

    @Override
    public void addCouponIssue(Long memberId, Coupon coupon, LocalDateTime currentTime){
        CouponIssue couponIssue = CouponIssue.create(memberId, coupon, currentTime);
        couponIssueOutputPort.save(couponIssue);
    }

}
