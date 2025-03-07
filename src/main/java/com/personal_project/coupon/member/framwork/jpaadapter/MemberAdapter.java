package com.personal_project.coupon.member.framwork.jpaadapter;

import com.personal_project.coupon.member.applicaion.outputport.MemberOutputPort;
import com.personal_project.coupon.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberAdapter implements MemberOutputPort {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public boolean checkEmail(String email){
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

}
