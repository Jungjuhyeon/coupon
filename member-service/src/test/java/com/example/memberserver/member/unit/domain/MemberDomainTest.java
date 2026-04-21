package com.example.memberserver.member.unit.domain;

import com.example.memberserver.member.domain.Member;
import com.example.memberserver.member.domain.enumeration.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberDomainTest {

    @Test
    @DisplayName("Member.create() 시 역할이 USER로 자동 설정된다")
    void create_sets_USER_role() {
        // when
        Member member = Member.create("test@test.com", "홍길동", "010-1234-5678", "encodedPw");

        // then
        assertThat(member.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("Member.create() 시 전달한 필드값이 정확히 저장된다")
    void create_stores_all_fields_correctly() {
        // when
        Member member = Member.create("test@test.com", "홍길동", "010-1234-5678", "encodedPw");

        // then
        assertThat(member.getEmail()).isEqualTo("test@test.com");
        assertThat(member.getName()).isEqualTo("홍길동");
        assertThat(member.getPhone()).isEqualTo("010-1234-5678");
        assertThat(member.getPassword()).isEqualTo("encodedPw");
    }
}
