package com.example.orderserver.order.framwork.web;

import com.example.common.global.exception.response.SuccessResponse;
import com.example.common.global.security.AuthPrincipal;
import com.example.orderserver.order.application.usecase.AddOrderUseCase;
import com.example.orderserver.order.application.usecase.InquiryOrderUseCase;
import com.example.orderserver.order.domain.model.document.OrderSummaryDocument;
import com.example.orderserver.order.framwork.web.request.OrderInputDTO;
import com.example.orderserver.order.framwork.web.response.OrderInfoOutPutDTO;
import com.example.orderserver.order.framwork.web.response.OrderOutputDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final AddOrderUseCase addOrderUseCase;
    private final InquiryOrderUseCase inquiryOrderUseCase;

    @PostMapping("/create/{storeId}")
    public SuccessResponse<OrderOutputDTO> create(@AuthenticationPrincipal AuthPrincipal principal,
                                                  @PathVariable Long storeId,
                                                  @RequestBody OrderInputDTO request) throws JsonProcessingException {
        Long memberId = principal.getId();
        OrderOutputDTO response =addOrderUseCase.create(memberId, storeId, request);

        return SuccessResponse.success(response);
    }

    @GetMapping("/{orderId}")
    public SuccessResponse<OrderInfoOutPutDTO> getOrderDetail(@AuthenticationPrincipal AuthPrincipal principal,
                                                              @PathVariable Long orderId){
        Long memberId = principal.getId();
        OrderInfoOutPutDTO response = inquiryOrderUseCase.getOrderDetail(memberId,orderId);

        return SuccessResponse.success(response);
    }

    @GetMapping("")
    public SuccessResponse<List<OrderSummaryDocument>> getOrder(@AuthenticationPrincipal AuthPrincipal principal){

        Long memberId = principal.getId();
        List<OrderSummaryDocument> response = inquiryOrderUseCase.getOrder(memberId);

        return SuccessResponse.success(response);
    }

}
