package com.example.couponserver.coupon.application.inputport;

import com.example.common.global.exception.BusinessException;
import com.example.couponserver.coupon.application.outputport.CouponIssueOutputPort;
import com.example.couponserver.coupon.application.usecase.InquiryCouponIssueUseCase;
import com.example.couponserver.coupon.domain.model.CouponIssue;
import com.example.couponserver.coupon.exception.CouponErrorCode;
import com.example.couponserver.coupon.framework.web.response.CouponIssueOrderFeignDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class InquiryCouponIssueInputPort implements InquiryCouponIssueUseCase {
    private final CouponIssueOutputPort couponIssueOutputPort;
    @Override
    public CouponIssueOrderFeignDTO getCouponIssueValue(Long couponIssueId) {
        CouponIssue couponIssue = couponIssueOutputPort.findById(couponIssueId)
                .orElseThrow(()-> new BusinessException(CouponErrorCode.COUPON_NOT_FOUND));
        //사용가능한 쿠폰인지 검증
        validateUsable(couponIssue);

        return CouponIssueOrderFeignDTO.mapToDTO(couponIssueId,couponIssue.getCoupon().getDiscountValue());
    }

    private void validateUsable(CouponIssue couponIssue) {
        //사용여부
        if (!couponIssue.isUsable()) {
            throw new BusinessException(CouponErrorCode.COUPON_ALREADY_USED);
        }
        //사용기간 여부
        if (!couponIssue.getCoupon().isUsableNow(LocalDateTime.now())) {
            throw new BusinessException(CouponErrorCode.COUPON_EXPIRED);
        }
    }
}
