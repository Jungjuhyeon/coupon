package com.example.storeserver.store.infra.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberProfileFeignDTO {
    private Long id;
    private String email;
    private String phone;
    private String name;
}
