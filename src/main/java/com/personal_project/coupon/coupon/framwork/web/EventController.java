package com.personal_project.coupon.coupon.framwork.web;

import com.personal_project.coupon.coupon.application.usecase.AddEventUsecase;
import com.personal_project.coupon.coupon.framwork.web.request.EventInfoDTO;
import com.personal_project.coupon.coupon.framwork.web.response.EventOutPutDTO;
import com.personal_project.coupon.global.exception.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventController {

    private final AddEventUsecase addEventUsecase;
    @PostMapping("/create")
    public SuccessResponse<EventOutPutDTO> createCoupon(@RequestBody EventInfoDTO request){
        EventOutPutDTO response = addEventUsecase.addEvent(request);
        return SuccessResponse.success(response);
    }
}
