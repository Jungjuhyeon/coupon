package com.example.orderserver.order.application.inputport;

import com.example.orderserver.order.application.outputport.StatisticsOutputPort;
import com.example.orderserver.order.application.usecase.InquiryStatisticsUseCase;
import com.example.orderserver.order.framwork.web.response.MonthlyOrderStatisticsOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class InquiryStatisticsInputPort implements InquiryStatisticsUseCase {
    private final StatisticsOutputPort statisticsOutputPort;
    @Override
    public List<MonthlyOrderStatisticsOutPutDTO> getMonthlyOrderStatistics(LocalDateTime startDate,
                                                                           LocalDateTime endDate){
        return statisticsOutputPort.getMonthlyOrderStatistics(startDate, endDate);
    }

}
