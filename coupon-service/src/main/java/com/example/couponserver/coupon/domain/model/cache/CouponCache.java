package com.example.couponserver.coupon.domain.model.cache;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CouponCache {
    private final Long id;
    private final int stock;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public static CouponCache create(Long id,int stock,LocalDate startDate,LocalDate endDate){
        return CouponCache.builder()
                .id(id)
                .stock(stock)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }
    public boolean isValid(LocalDate now) {
        return !now.isBefore(startDate) && now.isBefore(endDate);
    }

}
