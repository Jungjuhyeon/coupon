package com.personal_project.coupon.member.framwork.web.response;

import com.personal_project.coupon.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberLoginOutputDTO {
    private Long id;
    private String email;
    private String accessToken;

    public static MemberLoginOutputDTO mapToDTO(Member member, String accessToken){
        return MemberLoginOutputDTO.builder()
                .id(member.getId())
                .email(member.getEmail())
                .accessToken(accessToken)
                .build();
    }
}
