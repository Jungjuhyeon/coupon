package com.personal_project.coupon.service;


import com.personal_project.coupon.coupon.application.service.sync.RedissonCouponIssueService;
import com.personal_project.coupon.coupon.application.usecase.IssueCouponUsecase;
import com.personal_project.coupon.coupon.domain.model.Coupon;
import com.personal_project.coupon.coupon.framwork.jpaadapter.CouponAdapter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class CouponIssueTest {
    @Autowired
    private IssueCouponUsecase issueCoupon;

    @Autowired
    private CouponAdapter couponAdapter;


    @Test
    void 동시에_1000개_요청() throws Exception {
        final Long eventId = 1L;
        final Long couponId = 1L;
        final int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final Long currentMemberId = i + 1L;
            executorService.submit(() -> {
                try {
                    issueCoupon.issue(eventId, couponId, currentMemberId);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        System.out.println("끝 actualTransactionActive = " + actualTransactionActive);

        Optional<Coupon> coupon = couponAdapter.findById(1L);


        assertThat(coupon.get().getIssuedQuantity()).isEqualTo(1000);
    }
}

