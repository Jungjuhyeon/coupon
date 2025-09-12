package com.personal_project.coupon.coupon.application.inputport;

import com.personal_project.coupon.coupon.application.outputport.CouponCacheOutputPort;
import com.personal_project.coupon.coupon.application.outputport.CouponOutputPort;
import com.personal_project.coupon.coupon.application.outputport.PromotionOutputPort;
import com.personal_project.coupon.coupon.application.usecase.AddCouponUsecase;
import com.personal_project.coupon.coupon.domain.model.Coupon;
import com.personal_project.coupon.coupon.domain.model.Promotion;
import com.personal_project.coupon.coupon.framwork.web.request.CouponInfoDTO;
import com.personal_project.coupon.coupon.framwork.web.response.CouponOutPutDTO;
import com.personal_project.coupon.global.exception.BusinessException;
import com.personal_project.coupon.global.exception.errorcode.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddCouponInputPort implements AddCouponUsecase {
    private final CouponOutputPort couponOutputPort;
    private final PromotionOutputPort promotionOutputPort;
    private final CouponCacheOutputPort couponCacheOutputPort;

    @Transactional
    public CouponOutPutDTO AddCoupon(CouponInfoDTO couponInfoDTO){

        Promotion promotion = promotionOutputPort.findById(couponInfoDTO.getPromotionId()).orElseThrow(()->new BusinessException(CommonErrorCode.PROMOTION_NOT_FOUND));

        Coupon coupon = Coupon.create(promotion,couponInfoDTO);

        Coupon save = couponOutputPort.save(coupon);

        couponCacheOutputPort.saveCouponData(coupon.getId(),
                couponInfoDTO.getMaxQuantity(),
                couponInfoDTO.getStartDate(),
                couponInfoDTO.getEndDate());

        return CouponOutPutDTO.mapToDTO(save);
    }
}
