package com.example.memberserver.member.infra;

import com.example.memberserver.member.domain.Member;
import com.example.memberserver.member.infra.persistence.MemberJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@EnableJpaAuditing
class MemberRepositoryIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void datasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Test
    @DisplayName("회원 저장 후 이메일로 조회 성공")
    void save_and_findByEmail() {
        // given
        Member member = Member.create("test@test.com", "홍길동", "010-1234-5678", "encoded");

        // when
        memberJpaRepository.save(member);
        Optional<Member> found = memberJpaRepository.findByEmail("test@test.com");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@test.com");
        assertThat(found.get().getName()).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("이메일 존재 여부 확인 - 존재하는 경우")
    void existsByEmail_true() {
        // given
        Member member = Member.create("exist@test.com", "이존재", "010-9999-8888", "encoded");
        memberJpaRepository.save(member);

        // when
        boolean exists = memberJpaRepository.existsByEmail("exist@test.com");

        // then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("이메일 존재 여부 확인 - 존재하지 않는 경우")
    void existsByEmail_false() {
        // when
        boolean exists = memberJpaRepository.existsByEmail("notexist@test.com");

        // then
        assertThat(exists).isFalse();
    }
}
