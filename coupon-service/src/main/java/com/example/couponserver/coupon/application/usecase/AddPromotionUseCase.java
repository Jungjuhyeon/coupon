package com.example.couponserver.coupon.application.usecase;


import com.example.couponserver.coupon.framework.web.request.PromotionIdInfoDTO;
import com.example.couponserver.coupon.framework.web.response.PromotionOutPutDTO;

public interface AddPromotionUseCase {
    PromotionOutPutDTO addPromotion(PromotionIdInfoDTO promotionIdInfoDTO);
}
