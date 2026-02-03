package com.example.memberserver.member.infra.persistence;

import com.example.memberserver.member.applicaion.outputport.MemberOutputPort;
import com.example.memberserver.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberAdapter implements MemberOutputPort {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public boolean existsByEmail(String email){
        return memberJpaRepository.existsByEmail(email);
    }

    @Override
    public Member save(Member member){
        return memberJpaRepository.save(member);
    }

    @Override
    public Optional<Member> findById(Long id){
        return memberJpaRepository.findById(id);
    }

    @Override
    public Optional<Member> findByEmail(String email){
        return memberJpaRepository.findByEmail(email);
    }
    @Override
    public boolean existsById(Long memberId){
        return memberJpaRepository.existsById(memberId);
    }

}
