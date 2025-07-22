package com.personal_project.coupon.coupon.application.inputport;


import com.personal_project.coupon.coupon.application.outputport.EventCacheOutPort;
import com.personal_project.coupon.coupon.application.outputport.EventOutport;
import com.personal_project.coupon.coupon.application.usecase.AddEventUsecase;
import com.personal_project.coupon.coupon.domain.model.Event;
import com.personal_project.coupon.coupon.framwork.web.request.EventInfoDTO;
import com.personal_project.coupon.coupon.framwork.web.response.EventOutPutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddEventInputPort implements AddEventUsecase {

    private final EventOutport eventOutport;
    private final EventCacheOutPort eventCacheOutPort;
    @Override
    @Transactional
    public EventOutPutDTO addEvent(EventInfoDTO eventInfoDTO){
        Event event = Event.create(eventInfoDTO);
        Event save = eventOutport.save(event);

        eventCacheOutPort.saveEventTime(save.getId(),eventInfoDTO);

        return EventOutPutDTO.mapToDTO(save);
    }

}
