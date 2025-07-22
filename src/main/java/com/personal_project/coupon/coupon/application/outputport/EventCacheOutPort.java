package com.personal_project.coupon.coupon.application.outputport;

import com.personal_project.coupon.coupon.domain.EventCache;
import com.personal_project.coupon.coupon.framwork.web.request.EventInfoDTO;


public interface EventCacheOutPort {
    void saveEventTime(Long eventId, EventInfoDTO eventInfoDTO);

    EventCache getEventCache(Long eventId);

    void deleteEventCache(Long eventId);

}
