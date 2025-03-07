package com.personal_project.coupon.member.applicaion.outputport;

import com.personal_project.coupon.member.domain.Member;

import java.util.Optional;

public interface MemberOutputPort {
    boolean checkEmail(String email);

    Member save(Member member);
    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);
}
