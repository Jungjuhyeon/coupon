package com.example.memberserver.member.application.inputport;

import com.example.common.global.exception.BusinessException;
import com.example.memberserver.member.application.outputport.MemberOutputPort;
import com.example.memberserver.member.domain.Member;
import com.example.memberserver.member.exception.MemberErrorCode;
import com.example.memberserver.member.framework.web.response.MemberProfileFeignDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InquiryMemberInputPortTest {

    @InjectMocks
    private InquiryMemberInputPort inquiryMemberInputPort;

    @Mock
    private MemberOutputPort memberOutputPort;

    @Test
    @DisplayName("회원 프로필 조회 성공")
    void getMemberProfile_success() {
        // given
        Long memberId = 1L;
        Member member = Member.create("test@test.com", "홍길동", "010-1234-5678", "encoded");
        when(memberOutputPort.findById(memberId)).thenReturn(Optional.of(member));

        // when
        MemberProfileFeignDTO result = inquiryMemberInputPort.getMemberProfile(memberId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@test.com");
        assertThat(result.getName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("존재하지 않는 회원 프로필 조회 실패")
    void getMemberProfile_fail_when_member_not_found() {
        // given
        Long memberId = 999L;
        when(memberOutputPort.findById(memberId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> inquiryMemberInputPort.getMemberProfile(memberId))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(MemberErrorCode.USER_EMAIL_NOT_FOUND));
    }
}
