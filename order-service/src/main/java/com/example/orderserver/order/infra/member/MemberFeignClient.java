package com.example.orderserver.order.infra.member;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "MEMBERSERVER")
public interface MemberFeignClient {
    @GetMapping("/internal/members/{memberId}")
    void validateMember(@PathVariable("memberId") Long memberId);

}
