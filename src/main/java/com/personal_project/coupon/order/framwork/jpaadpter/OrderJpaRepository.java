package com.personal_project.coupon.order.framwork.jpaadpter;

import com.personal_project.coupon.order.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
