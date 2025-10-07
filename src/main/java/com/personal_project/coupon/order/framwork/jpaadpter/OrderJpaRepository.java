package com.personal_project.coupon.order.framwork.jpaadpter;

import com.personal_project.coupon.order.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {


    @Query(""" 
            select o from Order o
            join fetch o.store s
            join fetch s.storeCategory
            join fetch s.brand
            where o.id = :orderId and o.member.id = :memberId""")
    public Optional<Order> findOrderDetail(@Param("memberId") Long memberId,
                                           @Param("orderId") Long orderId);

}
