package com.personal_project.coupon.member.framwork.jpaadapter;

import com.personal_project.coupon.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member,Long> {

    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);
}
