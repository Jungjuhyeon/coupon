package com.personal_project.coupon;

import com.personal_project.coupon.global.exception.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestController {

    @GetMapping("/test")

    public SuccessResponse<String> test(){
        log.info("success");
        return SuccessResponse.success("성공");
    }

}
