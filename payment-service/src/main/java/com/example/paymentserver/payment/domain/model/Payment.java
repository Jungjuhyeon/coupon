package com.example.paymentserver.payment.domain.model;

import com.example.common.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    private Integer amount; //결제금액

    private Payment(Long orderId, Integer amount){
        this.orderId = orderId;
        this.amount = amount;
    }

    public static Payment create(Long orderId, Integer amount){
        return new Payment(orderId,amount);
    }
}
