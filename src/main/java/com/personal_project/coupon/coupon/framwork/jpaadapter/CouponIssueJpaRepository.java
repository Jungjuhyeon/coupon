package com.personal_project.coupon.coupon.framwork.jpaadapter;

import com.personal_project.coupon.coupon.domain.CouponIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CouponIssueJpaRepository extends JpaRepository<CouponIssue,Long> {

    @Query("SELECT COUNT(c) > 0 FROM CouponIssue c WHERE c.member.id = :memberId AND c.coupon.id = :couponId")
    boolean existsByMemberIdAndCouponId(@Param("memberId") Long memberId, @Param("couponId") Long couponId);
}
