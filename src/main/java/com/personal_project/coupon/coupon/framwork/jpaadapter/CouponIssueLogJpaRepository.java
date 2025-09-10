package com.personal_project.coupon.coupon.framwork.jpaadapter;

import com.personal_project.coupon.coupon.domain.model.CouponIssueLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueLogJpaRepository extends JpaRepository<CouponIssueLog,Long> {
}
