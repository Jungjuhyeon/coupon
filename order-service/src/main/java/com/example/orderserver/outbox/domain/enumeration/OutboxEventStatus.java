package com.example.orderserver.outbox.domain.enumeration;

public enum OutboxEventStatus {
    READY_TO_PUBLISH,
    PUBLISHED,
    MESSAGE_CONSUME,
    FAILED
}
