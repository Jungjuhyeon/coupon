package com.example.orderserver.order.infra.persistence;

import com.example.orderserver.order.application.outputport.StatisticsOutputPort;
import com.example.orderserver.order.framework.web.response.MonthlyOrderStatisticsOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StatisticsAdapter implements StatisticsOutputPort {
    private final OrderJpaRepository orderJpaRepository;
    @Override
    public List<MonthlyOrderStatisticsOutPutDTO> getMonthlyOrderStatistics(LocalDateTime startDate,
                                                                           LocalDateTime endDate,
                                                                           Integer price){
        return orderJpaRepository.getMonthlyOrderStatistics(startDate,endDate,price);
    }

}
