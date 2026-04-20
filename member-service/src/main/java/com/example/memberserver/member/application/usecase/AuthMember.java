package com.example.memberserver.member.application.usecase;

import com.example.memberserver.member.domain.Member;
import com.example.memberserver.member.framework.web.request.MemberInfoDTO;
import com.example.memberserver.member.framework.web.request.MemberLoginDTO;
import com.example.memberserver.member.framework.web.response.MemberLoginOutputDTO;

public interface AuthMember {
    MemberLoginOutputDTO login(MemberLoginDTO request);
    Member signUp(MemberInfoDTO request);
}
