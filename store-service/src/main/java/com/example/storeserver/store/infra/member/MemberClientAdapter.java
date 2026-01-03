package com.example.storeserver.store.infra.member;

import com.example.storeserver.store.application.outputport.MemberOutputPort;
import com.example.storeserver.store.infra.member.dto.MemberProfileFeignDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberClientAdapter implements MemberOutputPort {
    private final MemberFeignClient memberFeignClient;
    @Override
    public boolean existsById(Long memberId) {
        return memberFeignClient.existsById(memberId);
    }
    @Override
    @CircuitBreaker(
            name = "store-circuit-breaker",
            fallbackMethod = "getOwnerInfoFallback"
    )
    public MemberProfileFeignDTO getOwnerInfo(Long ownerId) {
        return memberFeignClient.getMemberProfile(ownerId);
    }

    public MemberProfileFeignDTO getOwnerInfoFallback(Long ownerId, Throwable t) {
        return new MemberProfileFeignDTO(-1L, "익명", "익명", "익명");
    }
}
