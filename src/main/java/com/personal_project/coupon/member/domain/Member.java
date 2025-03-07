package com.personal_project.coupon.member.domain;


import com.personal_project.coupon.global.entity.BaseEntity;
import com.personal_project.coupon.member.domain.enumeration.Role;
import com.personal_project.coupon.member.framwork.web.request.MemberInfoDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String name;

    private String password;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member create(MemberInfoDTO memberInfo,String password){
        return Member.builder()
                .email(memberInfo.getEmail())
                .name(memberInfo.getName())
                .password(password)
                .phone(memberInfo.getPhone())
                .role(Role.USER)
                .build();
    }


}
