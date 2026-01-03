package com.example.couponserver.coupon.framwork.web;

import com.example.common.global.exception.response.SuccessResponse;
import com.example.couponserver.coupon.application.usecase.AddPromotionUsecase;
import com.example.couponserver.coupon.framwork.web.request.PromotionIdInfoDTO;
import com.example.couponserver.coupon.framwork.web.response.PromotionOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotions")
public class PromotionController {
    private final AddPromotionUsecase addPromotionUsecase;

    @PostMapping("/create")
    public SuccessResponse<PromotionOutPutDTO> createCoupon(@RequestBody PromotionIdInfoDTO request){
        PromotionOutPutDTO response = addPromotionUsecase.addPromotion(request);
        return SuccessResponse.success(response);
    }
}
