package com.personal_project.coupon.order.domain.model;


import com.personal_project.coupon.global.entity.BaseEntity;
import com.personal_project.coupon.store.domain.model.Menu;
import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    private Integer quantity;

    private Integer price;

    private Integer totalPrice;

    private OrderMenu(Order order, Menu menu, Integer quantity, Integer price, Integer totalPrice) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public static OrderMenu create(Order order, Menu menu,Integer quantity){
        Integer price = menu.getPrice();
        Integer totalPrice = price * quantity;
        return new OrderMenu(order, menu, quantity, price, totalPrice);
    }

    // 연관관계 메서드 (setter 대체)
    public void changeOrder(Order order) {
        this.order = order;
    }
}
