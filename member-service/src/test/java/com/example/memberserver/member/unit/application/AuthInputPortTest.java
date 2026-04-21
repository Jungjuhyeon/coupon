package com.example.memberserver.member.unit.application;

import com.example.common.global.exception.BusinessException;
import com.example.memberserver.member.application.inputport.AuthInputPort;
import com.example.memberserver.member.application.outputport.MemberOutputPort;
import com.example.memberserver.member.auth.jwt.JwtTokenGenerator;
import com.example.memberserver.member.domain.Member;
import com.example.memberserver.member.exception.MemberErrorCode;
import com.example.memberserver.member.framework.web.request.MemberInfoDTO;
import com.example.memberserver.member.framework.web.request.MemberLoginDTO;
import com.example.memberserver.member.framework.web.response.MemberLoginOutputDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthInputPortTest {

    @InjectMocks
    private AuthInputPort authInputPort;

    @Mock
    private MemberOutputPort memberOutputPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenGenerator jwtTokenGenerator;

    @Test
    @DisplayName("회원 가입 성공")
    void signUp_success() {
        // given
        // MemberInfoDTO: private 필드만 있으므로 ReflectionTestUtils로 직접 설정
        MemberInfoDTO request = new MemberInfoDTO();
        ReflectionTestUtils.setField(request, "email", "test@test.com");
        ReflectionTestUtils.setField(request, "password", "Password1!");
        ReflectionTestUtils.setField(request, "name", "홍길동");
        ReflectionTestUtils.setField(request, "phone", "010-1234-5678");

        Member savedMember = Member.create("test@test.com", "홍길동", "010-1234-5678", "encoded");
        when(memberOutputPort.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode("Password1!")).thenReturn("encoded");
        when(memberOutputPort.save(any(Member.class))).thenReturn(savedMember);

        // when
        Member result = authInputPort.signUp(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@test.com");
        assertThat(result.getName()).isEqualTo("홍길동");
        assertThat(result.getPhone()).isEqualTo("010-1234-5678");
        verify(memberOutputPort).save(any(Member.class));
    }

    @Test
    @DisplayName("중복 이메일로 회원 가입 실패")
    void signUp_fail_when_email_duplicated() {
        // given
        MemberInfoDTO request = new MemberInfoDTO();
        ReflectionTestUtils.setField(request, "email", "dup@test.com");
        when(memberOutputPort.existsByEmail("dup@test.com")).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> authInputPort.signUp(request))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(MemberErrorCode.DUPLICATE_EMAIL));
    }

    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        // given
        // MemberLoginDTO: private 필드만 있으므로 ReflectionTestUtils로 직접 설정
        MemberLoginDTO request = new MemberLoginDTO();
        ReflectionTestUtils.setField(request, "email", "test@test.com");
        ReflectionTestUtils.setField(request, "password", "Password1!");

        Member member = Member.create("test@test.com", "홍길동", "010-1234-5678", "encoded");
        when(memberOutputPort.findByEmail("test@test.com")).thenReturn(Optional.of(member));
        when(passwordEncoder.matches("Password1!", "encoded")).thenReturn(true);
        when(jwtTokenGenerator.createAccessToken(any(), anyString())).thenReturn("access-token");

        // when
        MemberLoginOutputDTO result = authInputPort.login(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isEqualTo("access-token");
        assertThat(result.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 실패")
    void login_fail_when_email_not_found() {
        // given
        MemberLoginDTO request = new MemberLoginDTO();
        ReflectionTestUtils.setField(request, "email", "notfound@test.com");
        when(memberOutputPort.findByEmail("notfound@test.com")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authInputPort.login(request))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(MemberErrorCode.USER_EMAIL_NOT_FOUND));
    }

    @Test
    @DisplayName("비밀번호 불일치로 로그인 실패")
    void login_fail_when_password_mismatch() {
        // given
        MemberLoginDTO request = new MemberLoginDTO();
        ReflectionTestUtils.setField(request, "email", "test@test.com");
        ReflectionTestUtils.setField(request, "password", "wrongPassword");

        Member member = Member.create("test@test.com", "홍길동", "010-1234-5678", "encoded");
        when(memberOutputPort.findByEmail("test@test.com")).thenReturn(Optional.of(member));
        when(passwordEncoder.matches("wrongPassword", "encoded")).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> authInputPort.login(request))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(MemberErrorCode.USER_PASSWORD_MISMATCH));
    }
}
