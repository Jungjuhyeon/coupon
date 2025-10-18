package com.personal_project.coupon.order.outbox.application.outputport;



import java.util.concurrent.CompletableFuture;

public interface OutboxEventSender {
    boolean supports(String eventType);// 어떤 이벤트 타입을 처리하는지
    CompletableFuture<?> send(String payload);           // 실제 발행 로직
}