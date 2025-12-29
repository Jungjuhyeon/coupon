package com.example.memberserver.member.framwork.web.response;

import com.example.memberserver.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberProfileFeignDTO {
    private Long id;
    private String email;
    private String phone;
    private String name;

    public static MemberProfileFeignDTO mapToDTO(Member member) {
        return builder()
                .id(member.getId())
                .email(member.getEmail())
                .phone(member.getPhone())
                .name(member.getName())
                .build();
    }
}
