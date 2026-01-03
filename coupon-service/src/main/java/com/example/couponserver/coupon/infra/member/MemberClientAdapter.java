package com.example.couponserver.coupon.infra.member;

import com.example.couponserver.coupon.application.outputport.MemberOutputPort;
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

}
