package com.personal_project.coupon.coupon.framwork.jpaadapter;

import com.personal_project.coupon.coupon.application.outputport.EventOutport;
import com.personal_project.coupon.coupon.domain.entity.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EventAdapter implements EventOutport {

    private final EventJpaRepository eventJpaRepository;

    @Override
    public Optional<Event> findById(Long eventId){
        return eventJpaRepository.findById(eventId);
    }

    @Override
    public Event save(Event event){
        return eventJpaRepository.save(event);
    }

}
