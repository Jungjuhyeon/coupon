package com.example.orderserver.order.application.inputport;


import com.example.orderserver.order.application.outputport.*;
import com.example.orderserver.order.application.usecase.AddOrderUseCase;
import com.example.orderserver.order.domain.model.Order;
import com.example.orderserver.order.domain.model.OrderMenu;
import com.example.orderserver.order.domain.model.event.OrderCreatedEvent;
import com.example.orderserver.order.framwork.web.request.OrderInputDTO;
import com.example.orderserver.order.framwork.web.request.OrderMenuInfoDTO;
import com.example.orderserver.order.framwork.web.response.OrderOutputDTO;
import com.example.orderserver.order.infra.coupon.dto.CouponIssueInfoFeignDTO;
import com.example.orderserver.order.infra.store.dto.response.MenuInfoFeignDTO;
import com.example.orderserver.order.outbox.domain.OutboxEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.orderserver.order.domain.model.Order.createOrderEvent;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AddOrderInputPort implements AddOrderUseCase {

    private final MemberOutputPort memberOutputPort;
    private final StoreOutputPort storeOutputPort;
    private final CouponOutputPort couponOutputPort;
    private final OrderOutputPort orderOutputPort;
    private final PaymentOutputPort paymentOutputPort;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    private final String eventType = "OrderCreated";
    private final String aggregateType = "Order";

    @Override
    @Transactional
    public OrderOutputDTO create(Long memberId, Long storeId, OrderInputDTO request) throws JsonProcessingException {

        validateMemberAndStore(memberId, storeId);

        CouponIssueInfoFeignDTO couponIssueInfo = loadCouponIfExists(request);

        Order order = Order.create(memberId, storeId, request.getDeliveryAddress(),request.getComment());

        List<OrderMenu> orderMenus = createOrderMenus(order, request);
        order.addOrderMenus(orderMenus);

        applyPricing(order, couponIssueInfo);

        orderOutputPort.save(order);

        paymentOutputPort.save(order.getId(),order.getFinalPrice());

        publishOrderCreatedEvent(order, memberId);

        return OrderOutputDTO.mapToDTO(order.getId());
    }

    private void validateMemberAndStore(Long memberId, Long storeId) {
        memberOutputPort.validateMember(memberId);
        storeOutputPort.validateStore(storeId);
    }

    private CouponIssueInfoFeignDTO loadCouponIfExists(OrderInputDTO request) {
        if (request.getCouponIssueId() == null) {
            return null;
        }
        return couponOutputPort.getCouponIssueInfo(request.getCouponIssueId());
    }

    private void applyPricing(Order order, CouponIssueInfoFeignDTO coupon) {
        if (coupon == null) {
            order.finalizePriceWithoutCoupon();
            return;
        }
        order.finalizePriceWithCoupon(coupon.getCouponIssueId(), coupon.getDiscountValue());
    }

    private void publishOrderCreatedEvent(Order order, Long memberId) throws JsonProcessingException {

        OrderCreatedEvent event = createOrderEvent(memberId, order.getId(),order.getCouponIssueId(), eventType);

        String payload = objectMapper.writeValueAsString(event);

        OutboxEvent outboxEvent = OutboxEvent.create(aggregateType, event.getOrderId(), event.getEventType(), payload);

        eventPublisher.publishEvent(outboxEvent);
    }
    private List<OrderMenu> createOrderMenus(Order order, OrderInputDTO request) {

        List<Long> menuIdList = extractMenuIds(request);
        Map<Long, Integer> menuQuantityMap = extractMenuQuantityMap(request);

        List<MenuInfoFeignDTO> menuInfos = storeOutputPort.getMenuInfoList(menuIdList);

        return menuInfos.stream()
                .map(menuInfo -> {
                    Integer quantity = menuQuantityMap.get(menuInfo.getMenuId());
                    return OrderMenu.create(order, menuInfo.getMenuId(), menuInfo.getPrice(), quantity);
                })
                .toList();
    }

    private List<Long> extractMenuIds(OrderInputDTO request) {
        return request.getOrderMenuInfoDTOList().stream()
                .map(OrderMenuInfoDTO::getMenuId)
                .toList();
    }

    private Map<Long, Integer> extractMenuQuantityMap(OrderInputDTO request) {
        return request.getOrderMenuInfoDTOList().stream()
                .collect(Collectors.toMap(
                        OrderMenuInfoDTO::getMenuId,
                        OrderMenuInfoDTO::getCount
                ));
    }
}
