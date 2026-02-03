package com.example.storeserver.store.infra.member;

import com.example.common.global.exception.BusinessException;
import com.example.storeserver.store.application.outputport.MemberOutputPort;
import com.example.storeserver.store.exception.StoreErrorCode;
import com.example.storeserver.store.infra.member.dto.MemberProfileFeignDTO;
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
            fallbackMethod = "existsOwnerFallback"
    )
    public boolean existsOwner(Long memberId) {
        return memberFeignClient.existsById(memberId);
    }
    public boolean existsOwnerFallback(Long memberId, Throwable ex) {
        // 정책에 따라 다름
        throw new BusinessException(StoreErrorCode.MEMBER_SERVICE_UNAVAILABLE);
    }

    @Override
    @CircuitBreaker(
            name = "member-service",
            fallbackMethod = "getOwnerInfoFallback"
    )
    public MemberProfileFeignDTO getOwnerInfo(Long ownerId) {
        return memberFeignClient.getMemberProfile(ownerId);
    }
    public MemberProfileFeignDTO getOwnerInfoFallback(Long ownerId, Throwable t) {
        return new MemberProfileFeignDTO(-1L, "익명", "익명", "익명");
    }
}
