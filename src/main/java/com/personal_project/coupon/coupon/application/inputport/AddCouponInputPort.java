package com.personal_project.coupon.coupon.application.inputport;

import com.personal_project.coupon.coupon.application.outputport.CouponCacheOutPort;
import com.personal_project.coupon.coupon.application.outputport.CouponOutPort;
import com.personal_project.coupon.coupon.application.outputport.EventOutport;
import com.personal_project.coupon.coupon.application.usecase.AddCouponUsecase;
import com.personal_project.coupon.coupon.domain.entity.Coupon;
import com.personal_project.coupon.coupon.domain.entity.Event;
import com.personal_project.coupon.coupon.framwork.web.request.CouponInfoDTO;
import com.personal_project.coupon.coupon.framwork.web.response.CouponOutPutDTO;
import com.personal_project.coupon.global.exception.BusinessException;
import com.personal_project.coupon.global.exception.errorcode.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddCouponInputPort implements AddCouponUsecase {
    private final CouponOutPort couponOutPort;
    private final EventOutport eventOutport;
    private final CouponCacheOutPort couponCacheOutPort;

    public CouponOutPutDTO AddCoupon(CouponInfoDTO couponInfoDTO){

        Event event = eventOutport.findById(couponInfoDTO.getEventId()).orElseThrow(()->new BusinessException(CommonErrorCode.EVENT_NOT_FOUND));

        Coupon coupon = Coupon.create(event,couponInfoDTO);

        Coupon save = couponOutPort.save(coupon);

        couponCacheOutPort.saveCouponData(coupon.getId(),
                couponInfoDTO.getMaxQuantity(),
                couponInfoDTO.getStartDate(),
                couponInfoDTO.getEndDate());

        return CouponOutPutDTO.mapToDTO(save);
    }
}
