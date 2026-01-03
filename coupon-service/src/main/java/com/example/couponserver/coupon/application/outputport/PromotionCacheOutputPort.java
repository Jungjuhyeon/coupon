package com.example.couponserver.coupon.application.outputport;


import com.example.couponserver.coupon.domain.model.cache.PromotionCache;
import com.example.couponserver.coupon.framwork.web.request.PromotionIdInfoDTO;

public interface PromotionCacheOutputPort {
    void savePromotionTime(Long promotionId, PromotionIdInfoDTO promotionIdInfoDTO);

    PromotionCache getPromotionCache(Long promotionId);

    void deletePromotionCache(Long promotionId);

}
