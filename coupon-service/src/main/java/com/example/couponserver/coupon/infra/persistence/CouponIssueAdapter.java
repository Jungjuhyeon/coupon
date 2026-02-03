package com.example.couponserver.coupon.infra.persistence;

import com.example.couponserver.coupon.application.outputport.CouponIssueOutputPort;
import com.example.couponserver.coupon.domain.model.CouponIssue;
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

}
