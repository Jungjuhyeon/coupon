package com.example.orderserver.order.application.outputport;

import com.example.orderserver.order.framwork.web.response.MonthlyOrderStatisticsOutPutDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsOutputPort {

    List<MonthlyOrderStatisticsOutPutDTO> getMonthlyOrderStatistics(LocalDateTime startDate,
                                                                    LocalDateTime endDate,
                                                                    Integer price);
}
