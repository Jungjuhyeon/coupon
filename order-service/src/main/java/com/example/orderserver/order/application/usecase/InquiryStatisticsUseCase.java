package com.example.orderserver.order.application.usecase;

import com.example.orderserver.order.framwork.web.response.MonthlyOrderStatisticsOutPutDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface InquiryStatisticsUseCase {
    List<MonthlyOrderStatisticsOutPutDTO> getMonthlyOrderStatistics(LocalDateTime startDate,
                                                                    LocalDateTime endDate,
                                                                    Integer price);
}
