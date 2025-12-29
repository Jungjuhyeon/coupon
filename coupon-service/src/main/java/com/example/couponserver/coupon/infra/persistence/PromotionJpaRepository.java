package com.example.couponserver.coupon.infra.persistence;


import com.example.couponserver.coupon.domain.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionJpaRepository extends JpaRepository<Promotion, Long> {
}
