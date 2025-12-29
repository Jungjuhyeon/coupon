package com.example.orderserver.order.infra.coupon;

import com.example.common.global.exception.BusinessException;
import com.example.orderserver.order.application.outputport.CouponOutputPort;
import com.example.orderserver.order.exception.OrderErrorCode;
import com.example.orderserver.order.infra.coupon.dto.CouponIssueInfoFeignDTO;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponClientAdapter implements CouponOutputPort {
    private final CouponFeignClient couponFeignClient;
    @Override
    public CouponIssueInfoFeignDTO getCouponIssueInfo(Long couponIssueId){
        try {
            return couponFeignClient.getCouponIssue(couponIssueId);
        } catch (FeignException.NotFound e) {
            throw new BusinessException(OrderErrorCode.ORDER_PRECONDITION_FAILED);
        }
    }
}
