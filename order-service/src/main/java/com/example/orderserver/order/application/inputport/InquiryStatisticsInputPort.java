package com.example.orderserver.order.application.inputport;

import com.example.orderserver.order.application.outputport.StatisticsOutputPort;
import com.example.orderserver.order.application.usecase.InquiryStatisticsUseCase;
import com.example.orderserver.order.framwork.web.response.MonthlyOrderStatisticsOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class InquiryStatisticsInputPort implements InquiryStatisticsUseCase {
    private final StatisticsOutputPort statisticsOutputPort;
    @Override
    public List<MonthlyOrderStatisticsOutPutDTO> getMonthlyOrderStatistics(LocalDateTime startDate,
                                                                           LocalDateTime endDate){
        List<LocalDateTime[]> monthRanges = splitByMonth(startDate, endDate);

        return monthRanges.parallelStream()
                .map(range -> statisticsOutputPort.getMonthlyOrderStatistics(range[0], range[1]))
                .flatMap(List::stream)
                .sorted((a, b) -> Integer.compare(a.getMonth(), b.getMonth()))
                .toList();
    }

    private List<LocalDateTime[]> splitByMonth(LocalDateTime start, LocalDateTime end) {
        List<LocalDateTime[]> ranges = new ArrayList<>();
        YearMonth currentYM = YearMonth.from(start);
        YearMonth endYM = YearMonth.from(end);

        while (!currentYM.isAfter(endYM)) {
            LocalDateTime monthStart = currentYM.atDay(1).atStartOfDay();
            LocalDateTime monthEnd = currentYM.atEndOfMonth().atTime(23, 59, 59, 999_999_999);
            ranges.add(new LocalDateTime[]{monthStart, monthEnd});
            currentYM = currentYM.plusMonths(1);
        }
        return ranges;
    }

}
