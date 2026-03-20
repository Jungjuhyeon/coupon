package com.example.orderserver.order.domain.model;

import com.example.common.global.entity.BaseEntity;
import com.example.orderserver.order.domain.model.enumeration.OrderStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_menu_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(nullable = false)
    private Long menuId;

    private Integer quantity;

    private Integer price;

    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private OrderMenu(Order order, Long menuId, Integer quantity, Integer price, Integer totalPrice) {
        this.order = order;
        this.menuId = menuId;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
        this.orderStatus = OrderStatus.PENDING;
    }

    public static OrderMenu create(Order order, Long menuId, Integer menuPrice,Integer quantity){
        Integer totalPrice = menuPrice * quantity;
        return new OrderMenu(order, menuId, quantity, menuPrice, totalPrice);
    }

    // 연관관계 메서드 (setter 대체)
    public void changeOrder(Order order) {
        this.order = order;
    }

    public void cancel() {
        this.orderStatus = OrderStatus.CANCELLED;
    }
    public void complete() {
        this.orderStatus = OrderStatus.COMPLETED;
    }
}
