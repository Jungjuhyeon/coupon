package com.example.couponserver.coupon.application.usecase;


import com.example.couponserver.coupon.framwork.web.request.CouponInfoDTO;
import com.example.couponserver.coupon.framwork.web.response.CouponOutPutDTO;

public interface AddCouponUsecase {
    CouponOutPutDTO AddCoupon(CouponInfoDTO couponInfoDTO);
}
