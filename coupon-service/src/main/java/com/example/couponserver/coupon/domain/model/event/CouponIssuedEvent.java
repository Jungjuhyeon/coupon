package com.example.couponserver.coupon.domain.model.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponIssuedEvent implements Serializable {

    private Long couponId;
    private Long memberId;
    private LocalDateTime currentTime;

}
