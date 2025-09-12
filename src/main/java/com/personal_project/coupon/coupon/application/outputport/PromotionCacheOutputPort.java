package com.personal_project.coupon.coupon.application.outputport;

import com.personal_project.coupon.coupon.domain.model.cache.PromotionCache;
import com.personal_project.coupon.coupon.framwork.web.request.PromotionIdInfoDTO;


public interface PromotionCacheOutputPort {
    void savePromotionTime(Long promotionId, PromotionIdInfoDTO promotionIdInfoDTO);

    PromotionCache getPromotionCache(Long promotionId);

    void deletePromotionCache(Long promotionId);

}
