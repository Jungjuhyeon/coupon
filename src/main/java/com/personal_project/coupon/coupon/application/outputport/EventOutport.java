package com.personal_project.coupon.coupon.application.outputport;

import com.personal_project.coupon.coupon.domain.Event;

import java.util.Optional;

public interface EventOutport {

    Optional<Event> findById(Long eventId);
}
