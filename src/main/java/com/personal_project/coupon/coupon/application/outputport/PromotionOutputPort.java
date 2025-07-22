package com.personal_project.coupon.coupon.application.outputport;

import com.personal_project.coupon.coupon.domain.model.Promotion;

import java.util.Optional;

public interface PromotionOutputPort {

    Optional<Promotion> findById(Long promotionId);

    Promotion save(Promotion promotion);
}
