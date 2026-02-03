package com.example.storeserver.store.application.outputport;

import com.example.storeserver.store.infra.member.dto.MemberProfileFeignDTO;

public interface MemberOutputPort {
    boolean existsOwner(Long memberId);
    MemberProfileFeignDTO getOwnerInfo(Long ownerId);
}
