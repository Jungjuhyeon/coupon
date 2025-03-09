package com.personal_project.coupon.member.framwork.web;

import com.personal_project.coupon.global.exception.response.SuccessResponse;
import com.personal_project.coupon.member.applicaion.usecase.AuthMember;
import com.personal_project.coupon.member.domain.Member;
import com.personal_project.coupon.member.framwork.web.request.MemberInfoDTO;
import com.personal_project.coupon.member.framwork.web.request.MemberLoginDTO;
import com.personal_project.coupon.member.framwork.web.response.MemberLoginOutputDTO;
import com.personal_project.coupon.member.framwork.web.response.MemberOutPutDTO;
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
