package com.example.couponserver.coupon.infra.persistence;

import com.example.couponserver.coupon.domain.model.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue,Long> {

    Optional<CouponIssue> findByIdAndMemberId(Long couponIssueId, Long memberId);
    @Modifying
    @Query(
    """
    UPDATE CouponIssue c
       SET c.couponIssueStatus = 'USED'
     WHERE c.id = :couponIssueId
       AND c.couponIssueStatus = 'ISSUED'
    """)
    int useCoupon(@Param("couponIssueId") Long couponIssueId);
}

