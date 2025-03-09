package com.personal_project.coupon.coupon.framwork.web;

import com.personal_project.coupon.coupon.application.usercase.IssueCoupon;
import com.personal_project.coupon.global.exception.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/event")
public class CouponController {

    private final IssueCoupon issueCoupon;
    @PostMapping("/{eventId}/issue")
    public SuccessResponse<String> issue(@PathVariable Long eventId,
                                         @RequestParam Long couponId,
                                         @AuthenticationPrincipal Long memberId
                                         ){
        issueCoupon.issue(eventId,couponId,memberId);
        return SuccessResponse.successWithoutResult("발급성공");
    }
}
