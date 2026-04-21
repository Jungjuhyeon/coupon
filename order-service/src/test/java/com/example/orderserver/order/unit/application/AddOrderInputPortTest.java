package com.example.orderserver.order.unit.application;

import com.example.common.global.exception.BusinessException;
import com.example.orderserver.order.application.inputport.AddOrderInputPort;
import com.example.orderserver.order.application.outputport.MemberOutputPort;
import com.example.orderserver.order.application.outputport.OrderOutputPort;
import com.example.orderserver.order.application.outputport.PaymentOutputPort;
import com.example.orderserver.order.application.outputport.StoreOutputPort;
import com.example.orderserver.order.application.service.CouponApplier;
import com.example.orderserver.order.application.service.OrderEventPublisher;
import com.example.orderserver.order.application.service.OrderMenuFactory;
import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.domain.model.OrderMenu;
import com.example.orderserver.order.exception.OrderErrorCode;
import com.example.orderserver.order.framework.web.request.OrderInputDTO;
import com.example.orderserver.order.framework.web.request.OrderMenuInfoDTO;
import com.example.orderserver.order.framework.web.response.OrderOutputDTO;
import com.example.orderserver.order.infra.coupon.dto.CouponIssueInfoFeignDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddOrderInputPortTest {

    @InjectMocks
    private AddOrderInputPort addOrderInputPort;

    @Mock
    private MemberOutputPort memberOutputPort;

    @Mock
    private StoreOutputPort storeOutputPort;

    @Mock
    private OrderOutputPort orderOutputPort;

    @Mock
    private PaymentOutputPort paymentOutputPort;

    @Mock
    private CouponApplier couponApplier;

    @Mock
    private OrderMenuFactory orderFactory;

    @Mock
    private OrderEventPublisher orderEventPublisher;

    @Test
    @DisplayName("쿠폰 없이 주문 생성 성공")
    void create_success_without_coupon() {
        // given
        Long memberId = 1L;
        Long storeId = 1L;

        OrderMenuInfoDTO menuInfoDTO = mock(OrderMenuInfoDTO.class);

        OrderInputDTO request = mock(OrderInputDTO.class);
        when(request.getCouponIssueId()).thenReturn(null);
        when(request.getDeliveryAddress()).thenReturn("서울시 강남구");
        when(request.getComment()).thenReturn("문 앞에 놔주세요");
        when(request.getOrderMenuInfoDTOList()).thenReturn(List.of(menuInfoDTO));

        doNothing().when(memberOutputPort).validateMember(memberId);
        doNothing().when(storeOutputPort).validateStore(storeId);
        when(couponApplier.loadCouponIfExists(null)).thenReturn(null);

        OrderMenu mockMenu = OrderMenu.create(mock(Order.class), 1L, 10000, 2);
        when(orderFactory.createOrderMenus(any(Order.class), any())).thenReturn(List.of(mockMenu));
        doNothing().when(couponApplier).applyPricing(any(Order.class), isNull());

        when(orderOutputPort.save(any(Order.class))).thenReturn(mock(Order.class));
        doNothing().when(paymentOutputPort).save(any(), any());
        doNothing().when(orderEventPublisher).publishOrderCreated(any(Order.class), any(Long.class));

        // when
        OrderOutputDTO result = addOrderInputPort.create(memberId, storeId, request);

        // then
        assertThat(result).isNotNull();
        verify(orderOutputPort).save(any(Order.class));
        verify(paymentOutputPort).save(any(), any());
    }

    @Test
    @DisplayName("쿠폰 적용하여 주문 생성 성공")
    void create_success_with_coupon() {
        // given
        Long memberId = 1L;
        Long storeId = 1L;
        Long couponIssueId = 10L;

        OrderMenuInfoDTO menuInfoDTO = mock(OrderMenuInfoDTO.class);

        OrderInputDTO request = mock(OrderInputDTO.class);
        when(request.getCouponIssueId()).thenReturn(couponIssueId);
        when(request.getDeliveryAddress()).thenReturn("서울시 강남구");
        when(request.getComment()).thenReturn("빠르게 부탁드려요");
        when(request.getOrderMenuInfoDTOList()).thenReturn(List.of(menuInfoDTO));

        CouponIssueInfoFeignDTO couponInfo = new CouponIssueInfoFeignDTO(couponIssueId, 3000);

        doNothing().when(memberOutputPort).validateMember(memberId);
        doNothing().when(storeOutputPort).validateStore(storeId);
        when(couponApplier.loadCouponIfExists(couponIssueId)).thenReturn(couponInfo);

        OrderMenu mockMenu = OrderMenu.create(mock(Order.class), 1L, 10000, 1);
        when(orderFactory.createOrderMenus(any(Order.class), any())).thenReturn(List.of(mockMenu));
        doNothing().when(couponApplier).applyPricing(any(Order.class), eq(couponInfo));

        when(orderOutputPort.save(any(Order.class))).thenReturn(mock(Order.class));
        doNothing().when(paymentOutputPort).save(any(), any());
        doNothing().when(orderEventPublisher).publishOrderCreated(any(Order.class), any(Long.class));

        // when
        OrderOutputDTO result = addOrderInputPort.create(memberId, storeId, request);

        // then
        assertThat(result).isNotNull();
        verify(couponApplier).loadCouponIfExists(couponIssueId);
    }

    @Test
    @DisplayName("존재하지 않는 회원으로 주문 생성 실패")
    void create_fail_when_member_not_found() {
        // given
        Long memberId = 999L;
        Long storeId = 1L;
        OrderInputDTO request = mock(OrderInputDTO.class);

        doThrow(new BusinessException(OrderErrorCode.MEMBER_NOT_FOUND))
                .when(memberOutputPort).validateMember(memberId);

        // when & then
        assertThatThrownBy(() -> addOrderInputPort.create(memberId, storeId, request))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(OrderErrorCode.MEMBER_NOT_FOUND));
    }

    @Test
    @DisplayName("존재하지 않는 가게로 주문 생성 실패")
    void create_fail_when_store_not_found() {
        // given
        Long memberId = 1L;
        Long storeId = 999L;
        OrderInputDTO request = mock(OrderInputDTO.class);

        doNothing().when(memberOutputPort).validateMember(memberId);
        doThrow(new BusinessException(OrderErrorCode.STORE_NOT_FOUND))
                .when(storeOutputPort).validateStore(storeId);

        // when & then
        assertThatThrownBy(() -> addOrderInputPort.create(memberId, storeId, request))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> assertThat(((BusinessException) ex).getErrorCode())
                        .isEqualTo(OrderErrorCode.STORE_NOT_FOUND));
    }
}
