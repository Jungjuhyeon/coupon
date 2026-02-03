package com.example.memberserver.member.applicaion.outputport;


import com.example.memberserver.member.domain.Member;

import java.util.Optional;

public interface MemberOutputPort {
    boolean existsByEmail(String email);
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    boolean existsById(Long memberId);
}
