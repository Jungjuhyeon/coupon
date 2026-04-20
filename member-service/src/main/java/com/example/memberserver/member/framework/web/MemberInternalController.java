package com.example.memberserver.member.framework.web;

import com.example.memberserver.member.application.usecase.InquiryMemberUseCase;
import com.example.memberserver.member.framework.web.response.MemberProfileFeignDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/members")
public class MemberInternalController {

    private final InquiryMemberUseCase inquiryMemberUseCase;
    @GetMapping("/{memberId}")
    public boolean existsById(@PathVariable Long memberId){
        return inquiryMemberUseCase.existsById(memberId);
    }

    @GetMapping("/profile/{memberId}")
    public MemberProfileFeignDTO getMemberProfile(@PathVariable Long memberId){
        return inquiryMemberUseCase.getMemberProfile(memberId);
    }

}
