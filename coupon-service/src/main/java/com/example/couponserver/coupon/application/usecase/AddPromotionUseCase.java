package com.example.couponserver.coupon.application.usecase;


import com.example.couponserver.coupon.framwork.web.request.PromotionIdInfoDTO;
import com.example.couponserver.coupon.framwork.web.response.PromotionOutPutDTO;

public interface AddPromotionUseCase {
    PromotionOutPutDTO addPromotion(PromotionIdInfoDTO promotionIdInfoDTO);
}
