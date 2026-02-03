package com.example.couponserver.coupon.application.inputport;

import com.example.couponserver.coupon.application.outputport.PromotionCacheOutputPort;
import com.example.couponserver.coupon.application.outputport.PromotionOutputPort;
import com.example.couponserver.coupon.application.usecase.AddPromotionUseCase;
import com.example.couponserver.coupon.domain.model.Promotion;
import com.example.couponserver.coupon.framwork.web.request.PromotionIdInfoDTO;
import com.example.couponserver.coupon.framwork.web.response.PromotionOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddPromotionInputPort implements AddPromotionUseCase {

    private final PromotionOutputPort promotionOutputPort;
    private final PromotionCacheOutputPort promotionCacheOutputPort;
    @Override
    @Transactional
    public PromotionOutPutDTO addPromotion(PromotionIdInfoDTO promotionIdInfoDTO){
        Promotion promotion = Promotion.create(promotionIdInfoDTO);
        Promotion save = promotionOutputPort.save(promotion);

        promotionCacheOutputPort.savePromotionTime(save.getId(), promotionIdInfoDTO);

        return PromotionOutPutDTO.mapToDTO(save);
    }

}
