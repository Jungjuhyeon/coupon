package com.example.memberserver.member.applicaion.usecase;

import com.example.memberserver.member.framwork.web.response.MemberProfileFeignDTO;

public interface InquiryMemberUseCase {
    boolean existsById(Long memberId);
    MemberProfileFeignDTO getMemberProfile(Long memberId);
}
