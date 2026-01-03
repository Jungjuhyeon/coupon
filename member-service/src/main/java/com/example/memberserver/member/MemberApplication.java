package com.example.memberserver.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(scanBasePackages = {
        "com.example.memberserver",
        "com.example.common"
})
@EnableJpaAuditing
public class MemberApplication {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MemberApplication.class, args);
    }
}