package com.example.memberserver.member.applicaion.inputport;

import com.example.common.global.exception.BusinessException;
import com.example.memberserver.member.exception.MemberErrorCode;
import com.example.memberserver.member.auth.jwt.JwtTokenGenerator;
import com.example.memberserver.member.applicaion.outputport.MemberOutputPort;
import com.example.memberserver.member.applicaion.usecase.AuthMember;
import com.example.memberserver.member.domain.Member;
import com.example.memberserver.member.framwork.web.request.MemberInfoDTO;
import com.example.memberserver.member.framwork.web.request.MemberLoginDTO;
import com.example.memberserver.member.framwork.web.response.MemberLoginOutputDTO;
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
    private final JwtTokenGenerator jwtTokenGenerator;

    @Override
    @Transactional
    public Member signUp(MemberInfoDTO request){
        //email 중복체크
        if(memberOutputPort.existsByEmail(request.getEmail())){
            throw new BusinessException(MemberErrorCode.DUPLICATE_EMAIL);
        };
        //비밀번호 암호화
        String encodePassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.create(request.getEmail(), request.getEmail(), request.getPhone() ,encodePassword);

        return memberOutputPort.save(member);

    }
    @Override
    public MemberLoginOutputDTO login(MemberLoginDTO request){

        Member member = memberOutputPort.findByEmail(request.getEmail())
                .orElseThrow(()-> new BusinessException(MemberErrorCode.USER_EMAIL_NOT_FOUND));
        //비밀번호 체크
        if(!passwordEncoder.matches(request.getPassword(),member.getPassword())){
            throw new BusinessException(MemberErrorCode.USER_PASSWORD_MISMATCH);
        }
        String accessToken = jwtTokenGenerator.createAccessToken(member.getId(),member.getRole().getKey());
        return MemberLoginOutputDTO.mapToDTO(member,accessToken);
    }


}
