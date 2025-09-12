package com.personal_project.coupon.coupon.application.inputport;


import com.personal_project.coupon.coupon.application.outputport.PromotionCacheOutputPort;
import com.personal_project.coupon.coupon.application.outputport.PromotionOutputPort;
import com.personal_project.coupon.coupon.application.usecase.AddPromotionUsecase;
import com.personal_project.coupon.coupon.domain.model.Promotion;
import com.personal_project.coupon.coupon.framwork.web.request.PromotionIdInfoDTO;
import com.personal_project.coupon.coupon.framwork.web.response.PromotionOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddPromotionInputPort implements AddPromotionUsecase {

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
