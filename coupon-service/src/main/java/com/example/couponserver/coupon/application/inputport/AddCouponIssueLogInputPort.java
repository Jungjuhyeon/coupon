package com.example.couponserver.coupon.application.inputport;

import com.example.couponserver.coupon.application.outputport.CouponIssueLogOutputPort;
import com.example.couponserver.coupon.application.usecase.AddCouponIssueLogUseCase;
import com.example.couponserver.coupon.domain.model.CouponIssueLog;
import com.example.couponserver.coupon.domain.model.event.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@RequiredArgsConstructor
@Service
@Transactional
public class AddCouponIssueLogInputPort implements AddCouponIssueLogUseCase {
    private final CouponIssueLogOutputPort couponIssueLogOutputPort;
    private final Queue<CouponIssueLog> buffer = new ConcurrentLinkedQueue<>();

    @Override
    public void addCouponIssue(Long memberId, Long couponId, EventType type){
        buffer.add(CouponIssueLog.create(memberId, couponId, type));
    }

    @Override
    public void flush() {
        List<CouponIssueLog> batch = new ArrayList<>();
        CouponIssueLog couponIssueLog;

        while ((couponIssueLog = buffer.poll()) != null) {
            batch.add(couponIssueLog);
        }
        if (!batch.isEmpty()) {
            couponIssueLogOutputPort.saveAll(batch);
        }
    }
}
