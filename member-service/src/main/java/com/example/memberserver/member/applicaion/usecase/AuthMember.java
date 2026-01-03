package com.example.memberserver.member.applicaion.usecase;

import com.example.memberserver.member.domain.Member;
import com.example.memberserver.member.framwork.web.request.MemberInfoDTO;
import com.example.memberserver.member.framwork.web.request.MemberLoginDTO;
import com.example.memberserver.member.framwork.web.response.MemberLoginOutputDTO;

public interface AuthMember {
    MemberLoginOutputDTO login(MemberLoginDTO request);
    Member signUp(MemberInfoDTO request);
}
