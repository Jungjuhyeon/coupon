package com.example.memberserver.member.application.usecase;

import com.example.memberserver.member.framework.web.response.MemberProfileFeignDTO;

public interface InquiryMemberUseCase {
    boolean existsById(Long memberId);
    MemberProfileFeignDTO getMemberProfile(Long memberId);
}
