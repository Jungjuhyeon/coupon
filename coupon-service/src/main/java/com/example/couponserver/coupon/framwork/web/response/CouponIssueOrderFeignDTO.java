package com.example.couponserver.coupon.framwork.web.response;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CouponIssueOrderFeignDTO {
    private Long couponIssueId;
    private int discountValue;

    public static CouponIssueOrderFeignDTO mapToDTO(Long couponIssueId, int discountValue) {
        return builder()
                .couponIssueId(couponIssueId)
                .discountValue(discountValue)
                .build();
    }
}
