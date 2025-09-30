package com.personal_project.coupon.coupon.framwork.jpaadapter;

import com.personal_project.coupon.coupon.application.outputport.CouponIssueOutputPort;
import com.personal_project.coupon.coupon.domain.model.CouponIssue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponIssueAdapter implements CouponIssueOutputPort {

    private final CouponIssueJpaRepository couponIssueJpaRepository;

    @Override
    public CouponIssue save(CouponIssue couponIssue){
        return couponIssueJpaRepository.save(couponIssue);
    }

    @Override
    public Optional<CouponIssue> findById(Long couponIssueId){
        return couponIssueJpaRepository.findById(couponIssueId);
    }

    @Override
    public Optional<CouponIssue> findByIdAndMemberId(Long couponIssueId,Long memberId){
        return couponIssueJpaRepository.findByIdAndMemberId(couponIssueId,memberId);
    }
}
