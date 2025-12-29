package com.example.storeserver.store.infra.member;

import com.example.storeserver.store.application.outputport.MemberOutputPort;
import com.example.storeserver.store.infra.member.dto.MemberProfileFeignDTO;
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
    public MemberProfileFeignDTO getOwnerInfo(Long ownerId){
        return memberFeignClient.getMemberProfile(ownerId);
    }
}
