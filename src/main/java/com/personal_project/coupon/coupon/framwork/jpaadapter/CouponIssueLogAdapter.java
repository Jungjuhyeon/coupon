package com.personal_project.coupon.coupon.framwork.jpaadapter;

import com.personal_project.coupon.coupon.application.outputport.CouponIssueLogOutputPort;
import com.personal_project.coupon.coupon.domain.model.CouponIssueLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponIssueLogAdapter implements CouponIssueLogOutputPort {

    private final CouponIssueLogJpaAdapter couponIssueLogJpaAdapter;

    @Override
    public void save(CouponIssueLog couponIssueLog){
        couponIssueLogJpaAdapter.save(couponIssueLog);
    }


    @Override
    public void saveAll(List<CouponIssueLog> batch){
        couponIssueLogJpaAdapter.saveAll(batch);
    }

}
