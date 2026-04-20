package com.example.orderserver.order.infra.persistence;

import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.framework.web.response.MonthlyOrderStatisticsOutPutDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
    @Query("""
        SELECT new com.example.orderserver.order.framework.web.response.MonthlyOrderStatisticsOutPutDTO(
            FUNCTION('MONTH', o.orderTime),
            COUNT(DISTINCT o.id),    
            COUNT(om.menuId),        
            SUM(om.totalPrice),           
            SUM(om.quantity)              
        )
        FROM Order o
        LEFT JOIN o.orderMenuList om      
        WHERE o.orderTime BETWEEN :startDate AND :endDate
        AND o.finalPrice > :price    
        GROUP BY FUNCTION('MONTH', o.orderTime)     
    """)
    List<MonthlyOrderStatisticsOutPutDTO> getMonthlyOrderStatistics(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("price") int price
    );
}
