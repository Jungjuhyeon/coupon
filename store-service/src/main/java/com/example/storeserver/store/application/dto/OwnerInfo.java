package com.example.storeserver.store.application.dto;

import com.example.storeserver.store.infra.member.dto.MemberProfileFeignDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OwnerInfo {
    private Long id;
    private String email;
    private String phone;
    private String name;

    public static OwnerInfo mapToDTO(MemberProfileFeignDTO response) {
        return OwnerInfo.builder()
                .id(response.getId())
                .email(response.getEmail())
                .phone(response.getPhone())
                .name(response.getName())
                .build();
    }
}
