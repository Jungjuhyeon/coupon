package com.example.storeserver.store.infra.member;

import com.example.storeserver.store.infra.member.dto.MemberProfileFeignDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "member-service", url = "${member.service.url}")
public interface MemberFeignClient {
    @GetMapping("/internal/members/{memberId}")
    boolean existsById(@PathVariable("memberId") Long memberId);

    @GetMapping("internal/members/profile/{memberId}")
    MemberProfileFeignDTO getMemberProfile(@PathVariable Long memberId);
}
