package com.example.orderserver.order.infra.persistence;

import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.framwork.web.response.MonthlyOrderStatisticsOutPutDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    @Query("""
        SELECT new com.example.orderserver.order.framwork.web.response.MonthlyOrderStatisticsOutPutDTO(
            FUNCTION('MONTH', o.orderTime),
            COUNT(DISTINCT o.id),
            SUM(o.finalPrice),
            COUNT(om.menuId),
            SUM(om.quantity)
        )
        FROM Order o
        JOIN o.orderMenuList om
        WHERE o.orderTime BETWEEN :startDate AND :endDate
        GROUP BY FUNCTION('MONTH', o.orderTime)
        ORDER BY FUNCTION('MONTH', o.orderTime)
    """)
    List<MonthlyOrderStatisticsOutPutDTO> getMonthlyOrderStatistics(
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
