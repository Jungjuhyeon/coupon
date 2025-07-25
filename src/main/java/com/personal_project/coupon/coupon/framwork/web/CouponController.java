package com.personal_project.coupon.coupon.framwork.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal_project.coupon.coupon.application.usecase.AddCouponUsecase;
import com.personal_project.coupon.coupon.application.usecase.IssueCouponUsecase;
import com.personal_project.coupon.coupon.framwork.web.request.CouponInfoDTO;
import com.personal_project.coupon.coupon.framwork.web.response.CouponOutPutDTO;
import com.personal_project.coupon.global.exception.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupons") // URL 변경
public class CouponController {

    private final IssueCouponUsecase issueCouponUsecase;
    private final AddCouponUsecase addCouponUsecase;

    @PostMapping("/issue")
    public SuccessResponse<String> issue(@RequestParam Long promotionId,
                                         @RequestParam Long couponId,
                                         @RequestParam Long memberId) throws JsonProcessingException {
        issueCouponUsecase.issue(promotionId,couponId,memberId);
        return SuccessResponse.successWithoutResult("발급성공");
    }

    @PostMapping("/create")
    public SuccessResponse<CouponOutPutDTO> create(@RequestBody CouponInfoDTO request){
        CouponOutPutDTO response = addCouponUsecase.AddCoupon(request);
        return SuccessResponse.success(response);
    }
}