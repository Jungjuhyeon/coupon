package com.personal_project.coupon.order.domain.model;

import com.personal_project.coupon.coupon.domain.model.CouponIssue;
import com.personal_project.coupon.global.entity.BaseEntity;
import com.personal_project.coupon.member.domain.Member;
import com.personal_project.coupon.order.domain.model.enumeration.OrderStatus;
import com.personal_project.coupon.store.domain.model.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_issue_id")
    private CouponIssue couponIssue;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderMenu> orderMenuList = new ArrayList<>();

    private Integer originalPrice;

    private Integer discountAmount;

    private Integer finalPrice;

    private String deliveryAddress;

    private LocalDateTime orderTime;

    private String comment;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private Order(Member member, Store store, CouponIssue couponIssue, String deliveryAddress, LocalDateTime orderTime, String comment){
        this.member = member;
        this.store = store;
        this.couponIssue = couponIssue;
        this.deliveryAddress = deliveryAddress;
        this.orderTime = orderTime;
        this.comment = comment;
        this.orderStatus = OrderStatus.PENDING;
    }

    public static Order create(Member member, Store store, CouponIssue couponIssue, String deliveryAddress, String comment){
        return new Order(
                member,
                store,
                couponIssue,
                deliveryAddress,
                LocalDateTime.now(),
                comment
        );
    }

    //원가 계산로직
    public Integer calculateOriginalPrice(){
        return orderMenuList.stream()
                .mapToInt(OrderMenu::getTotalPrice)
                .sum();
    }

    //가격 계산
    public void applyCoupon(CouponIssue couponIssue) {
        this.originalPrice = calculateOriginalPrice();
        this.couponIssue = couponIssue;
        this.discountAmount = couponIssue != null ? couponIssue.getCoupon().getDiscountValue() : 0;
        this.finalPrice = originalPrice - discountAmount;
    }

    // 연관관계 편의 메서드
    public void addOrderMenu(OrderMenu orderMenu) {
        this.orderMenuList.add(orderMenu);
        orderMenu.changeOrder(this); // setter 대신 연관관계 메서드 호출
    }

    // 연관관계 편의 메서드 (컬렉션)
    public void addOrderMenus(List<OrderMenu> orderMenus) {
        for (OrderMenu orderMenu : orderMenus) {
            addOrderMenu(orderMenu); // 단건 추가 재사용
        }
    }

}
