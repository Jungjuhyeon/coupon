package com.example.memberserver.member.framework.web;


import com.example.common.global.exception.response.SuccessResponse;
import com.example.memberserver.member.application.usecase.AuthMember;
import com.example.memberserver.member.domain.Member;
import com.example.memberserver.member.framework.web.request.MemberInfoDTO;
import com.example.memberserver.member.framework.web.request.MemberLoginDTO;
import com.example.memberserver.member.framework.web.response.MemberLoginOutputDTO;
import com.example.memberserver.member.framework.web.response.MemberOutPutDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {

    private final AuthMember authMember;

    @PostMapping("/signup")
    public SuccessResponse<MemberOutPutDTO> signUp(@Valid @RequestBody MemberInfoDTO request){
        Member member = authMember.signUp(request);
        return SuccessResponse.success(MemberOutPutDTO.mapToDTO(member));
    }

    @PostMapping("login")
    public SuccessResponse<MemberLoginOutputDTO> login(@Valid @RequestBody MemberLoginDTO request){
        MemberLoginOutputDTO response = authMember.login(request);
        return SuccessResponse.success(response);
    }

}
