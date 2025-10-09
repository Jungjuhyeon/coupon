package com.personal_project.coupon.order.domain.outbox;

import com.personal_project.coupon.order.domain.model.enumeration.OrderOutboxStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_outbox")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderOutbox {

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
    private OrderOutboxStatus status;

    private OrderOutbox(String aggregateType,Long aggregateId,String eventType,String payload,
                        LocalDateTime createdAt,OrderOutboxStatus status){
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.createdAt = createdAt;
        this.status = status;
    }
    public static OrderOutbox create(Long orderId,String eventType,String payload){
        return new OrderOutbox("Order", orderId, eventType, payload, LocalDateTime.now(), OrderOutboxStatus.READY_TO_PUBLISH);
    }

    public void markOutboxEventPending(){
        this.status = OrderOutboxStatus.PUBLISHED;
    }

    public void markOutboxEventFailed(){
        this.status = OrderOutboxStatus.FAILED;
    }

    public void markOutboxEventProcessed(){
        this.status = OrderOutboxStatus.MESSAGE_CONSUME;
    }

}