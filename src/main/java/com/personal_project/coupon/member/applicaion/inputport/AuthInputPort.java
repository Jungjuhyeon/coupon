package com.personal_project.coupon.member.applicaion.inputport;


import com.personal_project.coupon.global.exception.BusinessException;
import com.personal_project.coupon.global.exception.errorcode.CommonErrorCode;
import com.personal_project.coupon.member.applicaion.outputport.MemberOutputPort;
import com.personal_project.coupon.member.applicaion.usecase.AuthMember;
import com.personal_project.coupon.member.domain.Member;
import com.personal_project.coupon.member.framwork.web.request.MemberInfoDTO;
import com.personal_project.coupon.member.framwork.web.request.MemberLoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthInputPort implements AuthMember {
    private final MemberOutputPort memberOutputPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Member signUp(MemberInfoDTO request){
        //email 중복체크
        if(memberOutputPort.checkEmail(request.getEmail())){
            throw new BusinessException(CommonErrorCode.DUPLICATE_EMAIL);
        };
        //비밀번호 암호화
        String encodePassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.create(request,encodePassword);

        return memberOutputPort.save(member);

    }
    @Override
    public Member login(MemberLoginDTO request){

        Member member = memberOutputPort.findByEmail(request.getEmail()).orElseThrow(()-> new BusinessException(CommonErrorCode.USER_EMAIL_NOT_FOUND));
        //비밀번호 체크
        if(!passwordEncoder.matches(request.getPassword(),member.getPassword())){
            throw new BusinessException(CommonErrorCode.USER_PASSWORD_MISMATCH);
        }
        return member;
    }


}
