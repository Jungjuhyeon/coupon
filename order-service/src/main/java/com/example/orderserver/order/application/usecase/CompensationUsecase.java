package com.example.orderserver.order.application.usecase;

public interface CompensationUsecase {
    public void cancleOrder(Long oderId, Long memberId);
    public void successOrder(Long oderId, Long memberId);
}
