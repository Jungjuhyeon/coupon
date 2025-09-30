package com.personal_project.coupon.payment.domain.model;

import com.personal_project.coupon.global.entity.BaseEntity;
import com.personal_project.coupon.order.domain.model.Order;
import com.personal_project.coupon.payment.domain.model.enumeration.PaymentMethod;
import com.personal_project.coupon.payment.domain.model.enumeration.PaymentStatus;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

//    private PaymentMethod paymentMethod;

    private Integer amount; //결제금액

//    private PaymentStatus status;

//    private Long transactionId; //결제키

    private Payment(Order order, Integer amount){
        this.order = order;
        this.amount = amount;
    }

    public static Payment create(Order order, Integer amount){
        return new Payment(order,amount);
    }
}
