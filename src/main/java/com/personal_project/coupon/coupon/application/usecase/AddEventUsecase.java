package com.personal_project.coupon.coupon.application.usecase;

import com.personal_project.coupon.coupon.framwork.web.request.EventInfoDTO;
import com.personal_project.coupon.coupon.framwork.web.response.EventOutPutDTO;

public interface AddEventUsecase {

    EventOutPutDTO addEvent(EventInfoDTO eventInfoDTO);

}
