package com.example.orderserver.order.unit.application;

import com.example.common.global.exception.BusinessException;
import com.example.orderserver.order.application.inputport.CompensationInputPort;
import com.example.orderserver.order.application.outputport.OrderOutputPort;
import com.example.orderserver.order.application.outputport.OrderReadModelOutputPort;
import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.domain.model.enumeration.OrderStatus;
import com.example.orderserver.order.exception.OrderErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompensationInputPortTest {

    @InjectMocks
    private CompensationInputPort compensationInputPort;

    @Mock
    private OrderOutputPort orderOutputPort;

    @Mock
    private OrderReadModelOutputPort orderReadModelOutputPort;

    @Test
    @DisplayName("주문 취소(보상) 성공 - 주문 상태 CANCELLED로 변경")
    void cancleOrder_success() {
        // given
        Long orderId = 1L;
        Long memberId = 1L;
        Order order = Order.create(memberId, 1L, "서울시 강남구", "문 앞에 놔주세요");
        when(orderOutputPort.findById(orderId)).thenReturn(Optional.of(order));
        doNothing().when(orderReadModelOutputPort).updateStatus(orderId, "CANCELLED");

        // when
        compensationInputPort.cancleOrder(orderId, memberId);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCELLED);
        verify(orderReadModelOutputPort).updateStatus(orderId, "CANCELLED");
    }

    @Test
    @DisplayName("존재하지 않는 주문 취소 실패")
    void cancleOrder_fail_when_order_not_found() {
        // given
        Long orderId = 999L;
        Long memberId = 1L;
        when(orderOutputPort.findById(orderId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> compensationInputPort.cancleOrder(orderId, memberId))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(OrderErrorCode.ORDER_NOT_FOUND));
    }

    @Test
    @DisplayName("주문 완료(보상) 성공 - 주문 상태 COMPLETED로 변경")
    void successOrder_success() {
        // given
        Long orderId = 1L;
        Long memberId = 1L;
        Order order = Order.create(memberId, 1L, "서울시 강남구", "문 앞에 놔주세요");
        when(orderOutputPort.findById(orderId)).thenReturn(Optional.of(order));
        doNothing().when(orderReadModelOutputPort).updateStatus(orderId, "COMPLETED");

        // when
        compensationInputPort.successOrder(orderId, memberId);

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.COMPLETED);
        verify(orderReadModelOutputPort).updateStatus(orderId, "COMPLETED");
    }
}
