package com.example.orderserver.order.application.inputport;


import com.example.orderserver.order.application.outputport.*;
import com.example.orderserver.order.application.service.CouponApplier;
import com.example.orderserver.order.application.service.OrderEventPublisher;
import com.example.orderserver.order.application.service.OrderMenuFactory;
import com.example.orderserver.order.application.usecase.AddOrderUseCase;
import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.domain.model.OrderMenu;
import com.example.orderserver.order.framwork.web.request.OrderInputDTO;
import com.example.orderserver.order.framwork.web.response.OrderOutputDTO;
import com.example.orderserver.order.infra.coupon.dto.CouponIssueInfoFeignDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddOrderInputPort implements AddOrderUseCase {

    private final MemberOutputPort memberOutputPort;
    private final StoreOutputPort storeOutputPort;
    private final OrderOutputPort orderOutputPort;
    private final PaymentOutputPort paymentOutputPort;
    private final CouponApplier couponApplier;
    private final OrderMenuFactory orderFactory;
    private final OrderEventPublisher orderEventPublisher;

    @Override
    @Transactional
    public OrderOutputDTO create(Long memberId, Long storeId, OrderInputDTO request){

        // 유저, 가게 존재 여부 검증
        memberOutputPort.validateMember(memberId);
        storeOutputPort.validateStore(storeId);

        // 쿠폰 정보 로드
        CouponIssueInfoFeignDTO couponIssueInfo = couponApplier.loadCouponIfExists(request.getCouponIssueId());

        Order order = Order.create(memberId, storeId, request.getDeliveryAddress(),request.getComment());
        // 주문 메뉴 생성 및 추가
        List<OrderMenu> orderMenus = orderFactory.createOrderMenus(order, request.getOrderMenuInfoDTOList());
        order.addOrderMenus(orderMenus);
        // 가격 적용
        couponApplier.applyPricing(order, couponIssueInfo);

        orderOutputPort.save(order);
        paymentOutputPort.save(order.getId(),order.getFinalPrice());

        orderEventPublisher.publishOrderCreated(order, memberId);

        return OrderOutputDTO.mapToDTO(order.getId());
    }


}
