package com.personal_project.coupon.coupon.application.usecase;

import com.personal_project.coupon.coupon.framwork.web.request.PromotionIdInfoDTO;
import com.personal_project.coupon.coupon.framwork.web.response.PromotionOutPutDTO;

public interface AddPromotionUsecase {

    PromotionOutPutDTO addPromotion(PromotionIdInfoDTO promotionIdInfoDTO);

}
