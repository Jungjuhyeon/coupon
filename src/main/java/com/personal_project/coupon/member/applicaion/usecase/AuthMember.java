package com.personal_project.coupon.member.applicaion.usecase;

import com.personal_project.coupon.member.domain.Member;
import com.personal_project.coupon.member.framwork.web.request.MemberInfoDTO;
import com.personal_project.coupon.member.framwork.web.request.MemberLoginDTO;

public interface AuthMember {
    Member login(MemberLoginDTO request);

    Member signUp(MemberInfoDTO request);

}
