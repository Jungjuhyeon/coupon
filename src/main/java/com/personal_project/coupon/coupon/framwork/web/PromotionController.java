package com.personal_project.coupon.coupon.framwork.web;

import com.personal_project.coupon.coupon.application.usecase.AddPromotionUsecase;
import com.personal_project.coupon.coupon.framwork.web.request.PromotionIdInfoDTO;
import com.personal_project.coupon.coupon.framwork.web.response.PromotionOutPutDTO;
import com.personal_project.coupon.global.exception.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class PromotionController {

    private final AddPromotionUsecase addPromotionUsecase;
    @PostMapping("/create")
    public SuccessResponse<PromotionOutPutDTO> createCoupon(@RequestBody PromotionIdInfoDTO request){
        PromotionOutPutDTO response = addPromotionUsecase.addPromotion(request);
        return SuccessResponse.success(response);
    }
}
