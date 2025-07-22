package com.personal_project.coupon.coupon.application.usecase;

import com.personal_project.coupon.coupon.framwork.web.request.CouponInfoDTO;
import com.personal_project.coupon.coupon.framwork.web.response.CouponOutPutDTO;

public interface AddCouponUsecase {
    CouponOutPutDTO AddCoupon(CouponInfoDTO couponInfoDTO);
}
