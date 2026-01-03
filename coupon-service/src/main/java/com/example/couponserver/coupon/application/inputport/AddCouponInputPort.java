package com.example.couponserver.coupon.application.inputport;

import com.example.common.global.exception.BusinessException;
import com.example.couponserver.coupon.application.outputport.CouponCacheOutputPort;
import com.example.couponserver.coupon.application.outputport.CouponOutputPort;
import com.example.couponserver.coupon.application.outputport.PromotionOutputPort;
import com.example.couponserver.coupon.application.usecase.AddCouponUsecase;
import com.example.couponserver.coupon.domain.model.Coupon;
import com.example.couponserver.coupon.domain.model.Promotion;
import com.example.couponserver.coupon.exception.CouponErrorCode;
import com.example.couponserver.coupon.framwork.web.request.CouponInfoDTO;
import com.example.couponserver.coupon.framwork.web.response.CouponOutPutDTO;
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

        Promotion promotion = promotionOutputPort.findById(couponInfoDTO.getPromotionId())
                .orElseThrow(()->new BusinessException(CouponErrorCode.PROMOTION_NOT_FOUND));

        Coupon coupon = Coupon.create(promotion,couponInfoDTO);

        Coupon save = couponOutputPort.save(coupon);

        couponCacheOutputPort.saveCouponData(coupon.getId(),
                couponInfoDTO.getMaxQuantity(),
                couponInfoDTO.getStartDate(),
                couponInfoDTO.getEndDate());

        return CouponOutPutDTO.mapToDTO(save);
    }
}
