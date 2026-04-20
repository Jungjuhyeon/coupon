package com.example.couponserver.coupon.framework.web;

import com.example.common.global.exception.response.SuccessResponse;
import com.example.couponserver.coupon.application.usecase.AddCouponUseCase;
import com.example.couponserver.coupon.application.usecase.IssueCouponUseCase;
import com.example.couponserver.coupon.framework.web.request.CouponInfoDTO;
import com.example.couponserver.coupon.framework.web.response.CouponOutPutDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons") // URL 변경
public class CouponController {

    private final IssueCouponUseCase issueCouponUsecase;
    private final AddCouponUseCase addCouponUsecase;

    @PostMapping("/issue")
    public SuccessResponse<String> issue(@RequestParam Long promotionId,
                                         @RequestParam Long couponId,
                                         @RequestParam Long memberId) throws JsonProcessingException {
        issueCouponUsecase.issue(promotionId,couponId,memberId);
        return SuccessResponse.successWithoutResult("발급성공");
    }

    @PostMapping("/create")
    public SuccessResponse<CouponOutPutDTO> create(@RequestBody CouponInfoDTO request){
        CouponOutPutDTO response = addCouponUsecase.addCoupon(request);
        return SuccessResponse.success(response);
    }
}