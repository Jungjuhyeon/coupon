package com.example.memberserver.member.framework.web.response;

import com.example.memberserver.member.domain.Member;
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
