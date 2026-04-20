package com.example.couponserver.coupon.application.usecase;


import com.example.couponserver.coupon.framework.web.request.CouponInfoDTO;
import com.example.couponserver.coupon.framework.web.response.CouponOutPutDTO;

public interface AddCouponUseCase {
    CouponOutPutDTO addCoupon(CouponInfoDTO couponInfoDTO);
}
