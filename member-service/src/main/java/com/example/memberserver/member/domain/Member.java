package com.example.memberserver.member.domain;



import com.example.common.global.entity.BaseEntity;
import com.example.memberserver.member.domain.enumeration.Role;
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
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String name;
    private String password;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Member(String email, String name, String phone, String password, Role role) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }
    public static Member create(String email, String name, String phone, String password){
        return new Member(email, name, phone, password, Role.USER);
    }


}
