package com.example.orderserver.order.framwork.web;

import com.example.common.global.exception.response.SuccessResponse;
import com.example.orderserver.order.application.usecase.InquiryStatisticsUseCase;
import com.example.orderserver.order.framwork.web.response.MonthlyOrderStatisticsOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    private final InquiryStatisticsUseCase inquiryStatisticsUseCase;

    @GetMapping("/monthly-order")
    public SuccessResponse<List<MonthlyOrderStatisticsOutPutDTO>> getMonthlyOrderStatistics(@RequestParam("startDate") String startDateStr,
                                                                                            @RequestParam("endDate") String endDateStr) {

        LocalDateTime startDate = LocalDate.parse(startDateStr).atStartOfDay();
        LocalDateTime endDate = LocalDate.parse(endDateStr).atTime(23,59,59);

        List<MonthlyOrderStatisticsOutPutDTO> response =
                inquiryStatisticsUseCase.getMonthlyOrderStatistics(startDate, endDate);
        return SuccessResponse.success(response);
    }
}
