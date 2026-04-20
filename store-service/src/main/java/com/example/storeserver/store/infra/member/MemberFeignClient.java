package com.example.storeserver.store.infra.member;

import com.example.storeserver.store.infra.member.dto.MemberProfileFeignDTO;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MEMBERSERVER")
public interface MemberFeignClient {
    @GetMapping("/internal/members/{memberId}")
    boolean existsById(@PathVariable("memberId") Long memberId);

    @GetMapping("/internal/members/profile/{memberId}")
    @Bulkhead(name = "store-circuit-breaker")
    MemberProfileFeignDTO getMemberProfile(@PathVariable Long memberId);
}
