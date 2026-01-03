package com.example.couponserver.coupon.infra.persistence;

import com.example.couponserver.coupon.application.outputport.PromotionOutputPort;
import com.example.couponserver.coupon.domain.model.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PromotionAdapter implements PromotionOutputPort {

    private final PromotionJpaRepository promotionJpaRepository;
    @Override
    public Optional<Promotion> findById(Long promotionId){
        return promotionJpaRepository.findById(promotionId);
    }

    @Override
    public Promotion save(Promotion promotion){
        return promotionJpaRepository.save(promotion);
    }

}
