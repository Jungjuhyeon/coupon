package com.example.couponserver.coupon.infra.persistence;

import com.example.couponserver.coupon.domain.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface CouponJpaRepository extends JpaRepository<Coupon,Long> {
    @Query(
    """
    select c.id
      from Coupon c
     where :curDate between c.startDate and c.endDate
    """)
    List<Long> findActiveCoupons(@Param("curDate") LocalDate curDate);
}
