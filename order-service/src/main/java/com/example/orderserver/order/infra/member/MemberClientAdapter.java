package com.example.orderserver.order.infra.member;

import com.example.common.global.exception.BusinessException;
import com.example.orderserver.order.application.outputport.MemberOutputPort;
import com.example.orderserver.order.exception.OrderErrorCode;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberClientAdapter implements MemberOutputPort {
    private final MemberFeignClient memberFeignClient;
    @Override
    @CircuitBreaker(
            name = "member-service",
            fallbackMethod = "validateMemberFallback"
    )
    public void validateMember(Long memberId){
        memberFeignClient.validateMember(memberId);
    }
    private void validateMemberFallback(Long memberId, Throwable throwable) {
        if (throwable instanceof FeignException feignEx && feignEx.status() == 404) {
            throw new BusinessException(OrderErrorCode.MEMBER_NOT_FOUND);
        }
        // 로깅, 기본 처리, 대체 로직 가능
        throw new BusinessException(OrderErrorCode.MEMBER_SERVICE_UNAVAILABLE);
    }
}
