package com.example.orderserver.order.infra.coupon;

import com.example.common.global.exception.BusinessException;
import com.example.orderserver.order.application.outputport.CouponOutputPort;
import com.example.orderserver.order.exception.OrderErrorCode;
import com.example.orderserver.order.infra.coupon.dto.CouponIssueInfoFeignDTO;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponClientAdapter implements CouponOutputPort {
    private final CouponFeignClient couponFeignClient;
    @Override
    @CircuitBreaker(
            name = "coupon-service",
            fallbackMethod = "couponFallback"
    )
    public CouponIssueInfoFeignDTO getCouponIssueInfo(Long couponIssueId){
        return couponFeignClient.getCouponIssue(couponIssueId);
    }
    private CouponIssueInfoFeignDTO couponFallback(Long couponIssueId, Throwable throwable) {
        if (throwable instanceof FeignException feignEx) {
            int status = feignEx.status();

            switch (status) {
                case 400: // 기간 만료/전
                    throw new BusinessException(OrderErrorCode.COUPON_EXPIRED);
                case 404: // 존재하지 않음
                    throw new BusinessException(OrderErrorCode.COUPON_NOT_FOUND);
                case 409: // 이미 사용됨
                    throw new BusinessException(OrderErrorCode.COUPON_ALREADY_USED);
                default:
                    // 다른 4xx, 5xx 에러 처리
                    throw new BusinessException(OrderErrorCode.COUPON_SERVICE_UNAVAILABLE);
            }
        }
        throw new BusinessException(OrderErrorCode.COUPON_SERVICE_UNAVAILABLE);
    }
}
