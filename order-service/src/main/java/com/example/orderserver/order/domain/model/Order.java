package com.example.orderserver.order.domain.model;

import com.example.common.global.entity.BaseEntity;
import com.example.orderserver.order.domain.model.enumeration.OrderStatus;
import com.example.orderserver.order.domain.model.event.OrderCreatedEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long storeId;

    private Long couponIssueId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderMenu> orderMenuList = new ArrayList<>();

    private Integer originalPrice;

    private Integer discountValue;

    private Integer finalPrice;

    private String deliveryAddress;

    private LocalDateTime orderTime;

    private String comment;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private Order(Long memberId, Long storeId, String deliveryAddress, LocalDateTime orderTime, String comment){
        this.memberId = memberId;
        this.storeId = storeId;
        this.deliveryAddress = deliveryAddress;
        this.orderTime = orderTime;
        this.comment = comment;
        this.orderStatus = OrderStatus.PENDING;
    }

    public static Order create(Long memberId, Long storeId, String deliveryAddress, String comment){
        return new Order(memberId, storeId, deliveryAddress, LocalDateTime.now(), comment);
    }

    public static OrderCreatedEvent createOrderEvent(Long memberId, Long orderId,Long couponIssueId, String eventType){
        return new OrderCreatedEvent(orderId,memberId,couponIssueId,eventType);
    }

    // 쿠폰 있는 경우 가격 확정
    public void finalizePriceWithCoupon(Long couponIssueId, int discountValue) {
        this.couponIssueId = couponIssueId;
        calculateFinalPrice(discountValue);
    }

    // 쿠폰 없는 경우 가격 확정
    public void finalizePriceWithoutCoupon() {
        this.couponIssueId = null;
        calculateFinalPrice(0);
    }
    // 가격 계산
    private void calculateFinalPrice(int discountValue) {
        this.originalPrice = calculateOrderMenuTotalPrice();
        this.discountValue = discountValue;
        this.finalPrice = originalPrice - discountValue;
    }

    // 메뉴 원가 계산
    private int calculateOrderMenuTotalPrice() {
        return orderMenuList.stream()
                .mapToInt(OrderMenu::getTotalPrice)
                .sum();
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
