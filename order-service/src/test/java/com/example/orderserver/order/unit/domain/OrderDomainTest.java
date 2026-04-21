package com.example.orderserver.order.unit.domain;

import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.domain.model.OrderMenu;
import com.example.orderserver.order.domain.model.enumeration.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderDomainTest {

    @Test
    @DisplayName("쿠폰 적용 시 최종 가격 = 원가 - 할인금액")
    void finalizePriceWithCoupon() {
        // given
        Order order = Order.create(1L, 1L, "서울시 강남구", "문 앞에 놔주세요");
        OrderMenu menu1 = OrderMenu.create(order, 1L, 10000, 2); // 20000
        OrderMenu menu2 = OrderMenu.create(order, 2L, 5000, 1);  // 5000
        order.addOrderMenus(List.of(menu1, menu2));

        // when
        order.finalizePriceWithCoupon(1L, 3000);

        // then
        assertThat(order.getOriginalPrice()).isEqualTo(25000);
        assertThat(order.getDiscountValue()).isEqualTo(3000);
        assertThat(order.getFinalPrice()).isEqualTo(22000);
        assertThat(order.getCouponIssueId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("쿠폰 미적용 시 최종 가격 = 원가, 할인금액 = 0")
    void finalizePriceWithoutCoupon() {
        // given
        Order order = Order.create(1L, 1L, "서울시 강남구", "문 앞에 놔주세요");
        OrderMenu menu = OrderMenu.create(order, 1L, 10000, 2); // 20000
        order.addOrderMenus(List.of(menu));

        // when
        order.finalizePriceWithoutCoupon();

        // then
        assertThat(order.getOriginalPrice()).isEqualTo(20000);
        assertThat(order.getDiscountValue()).isEqualTo(0);
        assertThat(order.getFinalPrice()).isEqualTo(20000);
        assertThat(order.getCouponIssueId()).isNull();
    }

    @Test
    @DisplayName("주문 취소 시 주문 상태와 모든 메뉴 상태가 CANCELLED로 변경")
    void cancel() {
        // given
        Order order = Order.create(1L, 1L, "서울시 강남구", "문 앞에 놔주세요");
        OrderMenu menu1 = OrderMenu.create(order, 1L, 10000, 1);
        OrderMenu menu2 = OrderMenu.create(order, 2L, 5000, 2);
        order.addOrderMenus(List.of(menu1, menu2));

        // when
        order.cancel();

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(menu1.getOrderStatus()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(menu2.getOrderStatus()).isEqualTo(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("주문 완료 시 주문 상태와 모든 메뉴 상태가 COMPLETED로 변경")
    void complete() {
        // given
        Order order = Order.create(1L, 1L, "서울시 강남구", "문 앞에 놔주세요");
        OrderMenu menu = OrderMenu.create(order, 1L, 10000, 1);
        order.addOrderMenus(List.of(menu));

        // when
        order.complete();

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETED);
        assertThat(menu.getOrderStatus()).isEqualTo(OrderStatus.COMPLETED);
    }
}
