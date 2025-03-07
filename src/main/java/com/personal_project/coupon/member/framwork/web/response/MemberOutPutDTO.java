package com.personal_project.coupon.member.framwork.web.response;

import com.personal_project.coupon.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberOutPutDTO {
    private Long id;
    private String email;

    public static MemberOutPutDTO mapToDTO(Member member){
        return MemberOutPutDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .build();
    }
}
