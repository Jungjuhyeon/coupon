package com.example.orderserver.order.infra.persistence;

import com.example.orderserver.order.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
//    @Query("""
//            select o from Order o
//            join fetch o.store s
//            join fetch s.storeCategory
//            join fetch s.brand
//            left join o.couponIssue ci
//            left join ci.coupon c
//            where o.id = :orderId and o.member.id = :memberId
//            """)
//    public Optional<Order> findOrder(@Param("memberId") Long memberId,
//                                     @Param("orderId") Long orderId);
}
