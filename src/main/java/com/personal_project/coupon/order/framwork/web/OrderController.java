package com.personal_project.coupon.order.framwork.web;

import com.personal_project.coupon.global.exception.response.SuccessResponse;
import com.personal_project.coupon.global.util.jwt.CustomUserDetails;
import com.personal_project.coupon.order.application.usecase.AddOrderUseCase;
import com.personal_project.coupon.order.framwork.web.request.OrderInputDTO;
import com.personal_project.coupon.order.framwork.web.response.OrderOutputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

    private final AddOrderUseCase addOrderUseCase;


    @PostMapping("/create/{storeId}")
    public SuccessResponse<OrderOutputDTO> create(@AuthenticationPrincipal CustomUserDetails member,
                                                  @PathVariable Long storeId,
                                                  @RequestBody OrderInputDTO request){
        Long memberId = member.getMemberId();
        OrderOutputDTO response =addOrderUseCase.create(memberId, storeId, request);

        return SuccessResponse.success(response);
    }

}
