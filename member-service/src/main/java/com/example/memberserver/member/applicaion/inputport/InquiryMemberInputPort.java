package com.example.memberserver.member.applicaion.inputport;

import com.example.common.global.exception.BusinessException;
import com.example.memberserver.member.applicaion.outputport.MemberOutputPort;
import com.example.memberserver.member.applicaion.usecase.InquiryMemberUseCase;
import com.example.memberserver.member.domain.Member;
import com.example.memberserver.member.exception.MemberErrorCode;
import com.example.memberserver.member.framwork.web.response.MemberProfileFeignDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryMemberInputPort implements InquiryMemberUseCase {
    private final MemberOutputPort memberOutputPort;
    @Override
    public boolean existsById(Long memberId){
        return memberOutputPort.existsById(memberId);
    }
    @Override
    public MemberProfileFeignDTO getMemberProfile(Long memberId){
        Member member = memberOutputPort.findById(memberId).
                orElseThrow(()-> new BusinessException(MemberErrorCode.USER_EMAIL_NOT_FOUND));
        return MemberProfileFeignDTO.mapToDTO(member);
    }
}
