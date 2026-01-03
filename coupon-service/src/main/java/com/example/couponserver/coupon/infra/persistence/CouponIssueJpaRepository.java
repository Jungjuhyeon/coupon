package com.example.couponserver.coupon.infra.persistence;

import com.example.couponserver.coupon.domain.model.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue,Long> {

    Optional<CouponIssue> findByIdAndMemberId(Long couponIssueId, Long memberId);

}

