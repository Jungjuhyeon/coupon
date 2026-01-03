package com.example.couponserver.coupon.infra.persistence;

import com.example.couponserver.coupon.domain.model.CouponIssueLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponIssueLogJpaRepository extends JpaRepository<CouponIssueLog,Long> {
}
