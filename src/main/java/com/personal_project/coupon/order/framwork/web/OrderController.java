package com.personal_project.coupon.order.framwork.web;

import com.personal_project.coupon.global.exception.response.SuccessResponse;
import com.personal_project.coupon.global.util.jwt.CustomUserDetails;
import com.personal_project.coupon.order.application.usecase.AddOrderUseCase;
import com.personal_project.coupon.order.application.usecase.InquiryOrderUseCase;
import com.personal_project.coupon.order.framwork.web.request.OrderInputDTO;
import com.personal_project.coupon.order.framwork.web.response.OrderInfoOutPutDTO;
import com.personal_project.coupon.order.framwork.web.response.OrderOutputDTO;
import com.personal_project.coupon.order.framwork.web.response.OrderSummaryOutputDTO;
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
    public SuccessResponse<OrderOutputDTO> create(@AuthenticationPrincipal CustomUserDetails member,
                                                  @PathVariable Long storeId,
                                                  @RequestBody OrderInputDTO request){
        Long memberId = member.getMemberId();
        OrderOutputDTO response =addOrderUseCase.create(memberId, storeId, request);

        return SuccessResponse.success(response);
    }

    @GetMapping("/{orderId}")
    public SuccessResponse<OrderInfoOutPutDTO> getOrderDetail(@AuthenticationPrincipal CustomUserDetails member,
                                                        @PathVariable Long orderId){
        Long memberId = member.getMemberId();
        OrderInfoOutPutDTO response = inquiryOrderUseCase.getOrderDetail(memberId,orderId);

        return SuccessResponse.success(response);
    }

    @GetMapping("")
    public SuccessResponse<List<OrderSummaryOutputDTO>> getOrder(@AuthenticationPrincipal CustomUserDetails member){

        Long memberId = member.getMemberId();
        List<OrderSummaryOutputDTO> response = inquiryOrderUseCase.getOrder(memberId);

        return SuccessResponse.success(response);
    }

}
