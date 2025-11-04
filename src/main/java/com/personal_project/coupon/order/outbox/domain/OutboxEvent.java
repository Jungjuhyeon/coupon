package com.personal_project.coupon.order.outbox.domain;

import com.personal_project.coupon.order.outbox.domain.enumeration.OutboxEventStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_event")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String aggregateType;
    private Long aggregateId;
    private String eventType;

    @Lob
    private String payload;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private OutboxEventStatus status;

    private OutboxEvent(String aggregateType, Long aggregateId, String eventType, String payload,
                        LocalDateTime createdAt, OutboxEventStatus status){
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.createdAt = createdAt;
        this.status = status;
    }
    public static OutboxEvent create(String aggregateType,Long aggregateId, String eventType, String payload){
        return new OutboxEvent(aggregateType, aggregateId, eventType, payload, LocalDateTime.now(), OutboxEventStatus.READY_TO_PUBLISH);
    }

    public void markOutboxEventPending(){
        this.status = OutboxEventStatus.PUBLISHED;
    }

    public void markOutboxEventFailed(){
        this.status = OutboxEventStatus.FAILED;
    }

    public void markOutboxEventProcessed(){
        this.status = OutboxEventStatus.MESSAGE_CONSUME;
    }

}