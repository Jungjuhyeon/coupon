package com.example.couponserver.coupon.application.outputport;


import com.example.couponserver.coupon.domain.model.Promotion;

import java.util.Optional;

public interface PromotionOutputPort {

    Optional<Promotion> findById(Long promotionId);

    Promotion save(Promotion promotion);
}
